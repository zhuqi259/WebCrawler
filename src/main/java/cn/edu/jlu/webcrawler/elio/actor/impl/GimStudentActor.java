package cn.edu.jlu.webcrawler.elio.actor.impl;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import cn.edu.jlu.webcrawler.elio.ext.Actor;
import cn.edu.jlu.webcrawler.elio.request.TaskRequest;
import cn.edu.jlu.webcrawler.elio.request.TaskRequestMeta;
import cn.edu.jlu.webcrawler.elio.response.TaskResponse;
import cn.edu.jlu.webcrawler.elio.util.RegexTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author zhuqi259
 */
@Actor
public class GimStudentActor extends UntypedActor {
    private Logger logger = LoggerFactory.getLogger("actor");
    @Inject
    @Named("httpActor")
    private ActorRef httpActor;
    @Inject
    @Named("gimStudentParseActor")
    private ActorRef gimStudentParseActor;

    private static final String prefix = "http://gim.jlu.edu.cn/glc/glc_sanzhu_print.jsp?menu=shenpibiao&stuno=";
    private static final int begin = 2009;
    private static final int end = 2015;

    private final String[] num = {"11", "12", "13", "14", "15", "21",
            "22", "23", "24", "25", "31", "32", "33", "34", "41", "42", "43",
            "44", "45", "51", "52", "53", "54", "55", "61", "62", "63", "64",
            "65", "71", "72", "73", "74", "75", "76", "77", "78", "81", "82",
            "91", "92", "93", "95", "96"};

    @Override
    public void onReceive(Object message) throws Exception {
        //处理 task 请求消息
        if (message instanceof TaskRequest) {
            //可以对当前request 的数据进行转发
            httpActor.tell(message, self());
        }
        //处理 http返回消息  这里主要是分页处理 ，可以做些参数变更
        else if (message instanceof TaskResponse) {
            TaskResponse response = (TaskResponse) message;
            gimStudentParseActor.tell(response, self());
            boolean paged = response.isPaged();
            if (!paged) {
                for (int i = begin; i <= end; i++) {
                    String head = String.valueOf(i);
                    // for(int str = 11; str <= 99; str++){
                    for (String str : num) {
                        sendRequestOnce(response, prefix + head + str, 1001, 1300);
                        sendRequestOnce(response, prefix + head + str, 2001, 2300);
                        sendRequestOnce(response, prefix + head + str, 4001, 4300);
                    }
                }
            }
        } else {
            unhandled(message);
        }
    }

    private void sendRequestOnce(TaskResponse response, String pre, int begin, int end) {
        for (int j = begin; j <= end; j++) {
            String url = pre + String.valueOf(j);
            // logger.info(">>>>>> " + url);
            TaskRequest request = new TaskRequest();
            request.setPaged(true);
            TaskRequestMeta requestMetaPage = response.getRequestMeta().clone();
            requestMetaPage.setUrl(url);
            request.setRequestMeta(requestMetaPage);
            httpActor.tell(request, self());
        }
    }
}
