package cn.edu.jlu.webcrawler.elio.parser.impl;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import cn.edu.jlu.webcrawler.elio.actor.base.BaseActor;
import cn.edu.jlu.webcrawler.elio.dao.ProxyInfoMySQLMapper;
import cn.edu.jlu.webcrawler.elio.model.ProxyInfo;
import cn.edu.jlu.webcrawler.elio.parser.Parser;
import cn.edu.jlu.webcrawler.elio.response.TaskResponse;
import cn.edu.jlu.webcrawler.elio.util.DateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhuqi259
 */
public class KuaiDaiLiParser extends BaseActor implements Parser<ProxyInfo> {
    private Logger logger = LoggerFactory.getLogger("parse");

    @Override
    public List<ProxyInfo> parse(String targetString) {
        List<ProxyInfo> proxyList = new ArrayList<>();
        Document doc = Jsoup.parse(targetString);
        String title = doc.select("title").text();
        Elements elements = doc.select("table[class=table table-bordered table-striped] tbody tr");
        Date now = new Date();
        for (Element element : elements) {
            Elements tds = element.select("td");
            String ip = tds.get(0).text();
            String port = tds.get(1).text();
            String type = tds.get(3).text();
            String position = tds.get(4).text();
            String checkTime = tds.get(6).text();

            Date checkDate = DateUtil.stringToDate(checkTime, "yyyy-MM-dd HH:mm:ss");
            // 过滤到超过60天的无效代理
            if (DateUtil.daysBetween(checkDate, now) > 60) {
                continue;
            }
            ProxyInfo proxyInfo = new ProxyInfo();
            proxyInfo.setPriority("1");
            proxyInfo.setProxyIp(ip);
            proxyInfo.setProxyPort(Integer.valueOf(port));
            proxyInfo.setProxyType(type);
            proxyInfo.setProxyPosition(position);
            proxyInfo.setCheckTime(checkTime);

            if (title.contains("国内")) {
                proxyInfo.setInter("0");
            } else {
                proxyInfo.setInter("1");
            }

            proxyList.add(proxyInfo);
        }
        return proxyList;
    }

    @Inject
    private ProxyInfoMySQLMapper proxyInfoMySQLMapper;

    @Override
    public void parallelProcess(ActorRef sender, Object message, ActorRef recipient, ActorContext context) {
        if (message instanceof TaskResponse) {
            TaskResponse response = (TaskResponse) message;
            String html = response.getResponseMeta().getBody();
            List<ProxyInfo> proxyList = this.parse(html);
            for (ProxyInfo proxyInfo : proxyList) {
                proxyInfoMySQLMapper.insertProxy(proxyInfo);
                System.out.println(proxyInfo);
            }
        }
    }

}
