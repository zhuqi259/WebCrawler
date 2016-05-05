package cn.edu.jlu.webcrawler.elio.proxy;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @author zhuqi259
 */
@Qualifier
public class DBProxyPool extends AbstractProxyPool implements ProxyPool {

    public DBProxyPool() {
    }

    public void initProxy() {
        logger.info("DBProxyPool..........initProxy.....begin....");
        // 需要自实现从数据读取方式  ip地址的格式 是192.168.1.1:80
        List<String> proxyList = proxyInfoMySQLMapper.getProxyList("");
        addProxy(proxyList);
        logger.info("DBProxyPool..........initProxy.....end....");
    }

}
