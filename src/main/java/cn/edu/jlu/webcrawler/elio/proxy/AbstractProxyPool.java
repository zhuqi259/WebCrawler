package cn.edu.jlu.webcrawler.elio.proxy;

import cn.edu.jlu.webcrawler.elio.dao.ProxyInfoMySQLMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * @author zhuqi259
 */
public abstract class AbstractProxyPool implements ProxyPool {
    protected Logger logger = LoggerFactory.getLogger("proxy");

    protected final static BlockingQueue<Proxy> proxyQueue = new DelayQueue<>();
    protected final static Map<String, Proxy> allProxy = new ConcurrentHashMap<>();

    protected final static String proxyFilePath = "config";
    protected final static String proxyFileName = "proxy.txt";

    protected final static int reuseInterval = 1500;// ms
    protected final static int reviveTime = 2 * 60 * 60 * 1000;// ms
    protected final static int saveProxyInterval = 10 * 60 * 1000;// ms

    protected boolean isEnable = false;
    protected boolean validateWhenInit = false;

    @Inject
    protected ProxyInfoMySQLMapper proxyInfoMySQLMapper;

    public AbstractProxyPool() {
    }

    abstract void initProxy();

    public void addProxy(Proxy proxy) {
        proxy.setFailedNum(0);
        proxy.setReuseTimeInterval(reuseInterval);
        proxyQueue.add(proxy);
        allProxy.put(proxy.getHttpHost(), proxy);
    }

    public void addProxy(Map<String, Proxy> httpProxyMap) {
        isEnable = true;
        for (Map.Entry<String, Proxy> entry : httpProxyMap.entrySet()) {
            try {
                if (allProxy.containsKey(entry.getKey())) {
                    continue;
                }
                Proxy proxy = entry.getValue();
                if (!validateWhenInit || ProxyUtils.validateProxy(proxy)) {
                    entry.getValue().setFailedNum(0);
                    entry.getValue().setReuseTimeInterval(reuseInterval);
                    proxyQueue.add(entry.getValue());
                    allProxy.put(entry.getKey(), entry.getValue());
                }
            } catch (NumberFormatException e) {
                logger.error("HttpHost init error:", e);
            }
        }
    }

    public void addProxy(List<String> httpProxyList) {
        isEnable = true;
        for (String proxyString : httpProxyList) {
            String[] proxy = proxyString.split(":");
            try {
                if (allProxy.containsKey(proxy[0] + "_" + proxy[1])) {
                    continue;
                }
                Proxy item = new Proxy(proxy[0], Integer.valueOf(proxy[1]));
                if (!validateWhenInit || ProxyUtils.validateProxy(item)) {
                    item.setReuseTimeInterval(reuseInterval);
                    proxyQueue.add(item);
                    allProxy.put(proxy[0] + "_" + proxy[1], item);
                }
            } catch (NumberFormatException e) {
                logger.error("HttpHost init error:", e);
            }
        }
        logger.info("proxy pool size>>>>" + allProxy.size());
    }

    public Proxy getProxyHost() {
        Proxy proxy = null;
        try {
            Long time = System.currentTimeMillis();
            proxy = proxyQueue.take();
            double costTime = (System.currentTimeMillis() - time) / 1000.0;
            if (costTime > reuseInterval) {
                logger.info("get proxy time >>>> " + costTime);
            }
            proxy.setLastBorrowTime(System.currentTimeMillis());
            proxy.borrowNumIncrement(1);
        } catch (InterruptedException e) {
            logger.error("get proxy error", e);
        }
        if (proxy == null) {
            throw new NoSuchElementException();
        }
        return proxy;
    }

    public void returnProxy(Proxy proxy) {
        // logger.info(">>> using DB Proxy Pool");
        if (proxy == null) {
            return;
        }
        int statusCode = proxy.getStateCode();
        String host = proxy.getHttpHost();
        switch (statusCode) {
            case Proxy.SUCCESS:
                proxy.setReuseTimeInterval(reuseInterval);
                proxy.setFailedNum(0);
                proxy.recordResponse();
                proxy.successNumIncrement(1);
                break;
            case Proxy.ERROR_403:
                proxy.fail(Proxy.ERROR_403);
                proxy.setReuseTimeInterval(reuseInterval * proxy.getFailedNum());
                logger.warn(host + " >>>> reuseTimeInterval is >>>> " + proxy.getReuseTimeInterval() / 1000.0);
                break;

            case Proxy.ERROR_404:
                proxy.fail(Proxy.ERROR_404);
                proxy.setReuseTimeInterval(reuseInterval * proxy.getFailedNum());
                logger.warn(host + " >>>> reuseTimeInterval is >>>> " + proxy.getReuseTimeInterval() / 1000.0);
                break;
            case Proxy.ERROR_500:
                proxy.fail(Proxy.ERROR_500);
                proxy.setReuseTimeInterval(10 * 60 * 1000 * proxy.getFailedNum());
                logger.warn("this proxy is banned >>>> " + proxy.getHttpHost());
                logger.info(host + " >>>> reuseTimeInterval is >>>> " + proxy.getReuseTimeInterval() / 1000.0);
                break;
            case Proxy.ERROR_502:
                proxy.fail(Proxy.ERROR_502);
                proxy.setReuseTimeInterval(100 * 60 * 1000 * proxy.getFailedNum());
                logger.warn("this proxy is banned >>>> " + proxy.getHttpHost());
                logger.info(host + " >>>> reuseTimeInterval is >>>> " + proxy.getReuseTimeInterval() / 1000.0);
                break;
            default:
                proxy.fail(statusCode);
                break;
        }
        if (proxy.getFailedNum() > 20) {
            proxy.setReuseTimeInterval(reviveTime);
            allProxy.remove(proxy.getHttpHost());
            logger.error("remove proxy >>>> " + proxy.getHttpHost() + ">>>>" + proxy.getStateCode() + " >>>> remain proxy >>>> " + proxyQueue.size());
        }
        if (proxy.getFailedNum() > 0 && proxy.getFailedNum() % 5 == 0) {
            if (!ProxyUtils.validateProxy(proxy)) {
                proxy.setReuseTimeInterval(reviveTime);
                allProxy.remove(proxy.getHttpHost());
                logger.error("remove proxy >>>> " + proxy.getHttpHost() + ">>>>" + proxy.getStateCode() + " >>>> remain proxy >>>> " + proxyQueue.size());
            }
        }

        try {
            proxyQueue.put(proxy);
        } catch (InterruptedException e) {
            logger.warn("proxyQueue return proxy error", e);
        }
    }

    protected static String allProxyStatus() {
        String re = "all proxy info >>>> \n";
        for (Map.Entry<String, Proxy> entry : allProxy.entrySet()) {
            re += entry.getValue().toString() + "\n";
        }
        return re;
    }

    public int getIdleNum() {
        return proxyQueue.size();
    }

    public int getReuseInterval() {
        return reuseInterval;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public int getReviveTime() {
        return reviveTime;
    }

    public boolean isValidateWhenInit() {
        return validateWhenInit;
    }

    public void validateWhenInit(boolean validateWhenInit) {
        this.validateWhenInit = validateWhenInit;
    }

    public int getSaveProxyInterval() {
        return saveProxyInterval;
    }
}
