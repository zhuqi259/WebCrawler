package cn.edu.jlu.webcrawler.elio.dao;

import cn.edu.jlu.webcrawler.elio.model.ProxyInfo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("proxyInfoMySQLMapper")
public interface ProxyInfoMySQLMapper {

    /**
     * 获取代理地址列表
     *
     * @param envType 环境类型  调试| 生产
     * @return List<String>
     */
    List<String> getProxyList(String envType);

    int insertProxy(ProxyInfo proxyInfo);
}
