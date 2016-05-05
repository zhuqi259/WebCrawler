package cn.edu.jlu.webcrawler.elio.actor.common;

import akka.actor.DeadLetter;
import akka.actor.UntypedActor;
import cn.edu.jlu.webcrawler.elio.ext.Actor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhuqi259
 *         监控  DeadLetter  方便akka调试
 */
@Actor
public class DeadLetterActor extends UntypedActor {

    protected Logger logger = LoggerFactory.getLogger("deadletter");

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DeadLetter) {
            DeadLetter deadLetter = (DeadLetter) message;
            logger.error(deadLetter.toString());
        }
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        getContext().system().eventStream().subscribe(self(), DeadLetter.class);
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        getContext().system().eventStream().unsubscribe(self());
    }
}
