package cn.edu.jlu.webcrawler.elio.proxy;

import java.util.List;
import java.util.Map;

/**
 * @author zhuqi259
 */
public interface ProxyPool {
    void addProxy(Proxy proxy);
    void addProxy(Map<String, Proxy> httpProxyMap);
    void addProxy(List<String> httpProxyList);
    Proxy getProxyHost();
    void returnProxy(Proxy proxy);
}
