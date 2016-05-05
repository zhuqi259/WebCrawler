package cn.edu.jlu.webcrawler.elio.task.impl;

import akka.actor.ActorRef;
import cn.edu.jlu.webcrawler.elio.request.TaskRequest;
import cn.edu.jlu.webcrawler.elio.task.SingleTask;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;

/**
 * @author zhuqi259
 */
@Qualifier
public class ProxyTask extends SingleTask {
    @Inject
    private ActorRef kuaiDaiLiActor;

    public ProxyTask() {
    }

    // @Scheduled(cron="0 0/1 *  * * ? ")   //spring task每一分钟执行一次 可以替换成quartz
    public void execute() {
        TaskRequest request = new TaskRequest();
        request.setRequestMeta(requestMeta);
        kuaiDaiLiActor.tell(request, ActorRef.noSender());
    }

}

