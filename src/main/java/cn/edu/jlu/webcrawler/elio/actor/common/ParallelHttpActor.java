package cn.edu.jlu.webcrawler.elio.actor.common;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import cn.edu.jlu.webcrawler.elio.actor.base.BaseActor;
import cn.edu.jlu.webcrawler.elio.ext.Actor;
import cn.edu.jlu.webcrawler.elio.proxy.Proxy;
import cn.edu.jlu.webcrawler.elio.proxy.ProxyPool;
import cn.edu.jlu.webcrawler.elio.proxy.UserAgentPool;
import cn.edu.jlu.webcrawler.elio.request.TaskRequest;
import cn.edu.jlu.webcrawler.elio.request.TaskRequestMeta;
import cn.edu.jlu.webcrawler.elio.response.TaskResponse;
import cn.edu.jlu.webcrawler.elio.response.TaskResponseMeta;
import cn.edu.jlu.webcrawler.elio.util.OkHttpTool;

import javax.inject.Inject;
import javax.inject.Named;
import java.security.SecureRandom;

/**
 * @author zhuqi259
 */
@Actor
public class ParallelHttpActor extends BaseActor {
    @Inject
    private UserAgentPool userAgentPool;
    /**
     * Spring自带的@Autowired的缺省情况等价于JSR-330的@Inject注解；
     * Spring自带的@Qualifier的缺省的根据Bean名字注入情况等价于JSR-330的@Named注解；
     * Spring自带的@Qualifier的扩展@Qualifier限定描述符注解情况等价于JSR-330的@Qualifier注解。
     */
    @Inject
    @Named("fileProxyPool")
    private ProxyPool proxyPool; //等效于@Autowired @Qualifier("xxx")

    @Override
    public void parallelProcess(ActorRef sender, Object message, ActorRef recipient, ActorContext context) {
        TaskRequest request = (TaskRequest) message;
        long crawlerId = System.currentTimeMillis() + Math.abs(new SecureRandom().nextInt());
        Proxy proxy = proxyPool.getProxyHost();
        request.setProxyHost(proxy.getHttpHost());
        request.setProxyPort(proxy.getPort());
        //失败重试  crawlerId不变
        if (!request.isRetry()) {
            request.setCrawlerId(crawlerId);
        }
        // 任务丢弃
        if (!request.isDiscard()) {
            if (request.getCurrentTryNum() == request.getMaxReTryNum()) {
                logger.error("任务超过重试次数 准备丢弃请求:" + request);
                request.setDiscard(true);
            }
            int deLaytime = request.getDelayTime();

            if (proxy == null) {
                request.setDelayTime(deLaytime + 100);
                logger.info(" get proxy error");
                sender.tell(request, ActorRef.noSender());
            }
            logger.info(request.toString());
            TaskRequestMeta requestMeta = request.getRequestMeta();
            TaskResponseMeta responseMeta;
            String userAgent;
            if (requestMeta.getIsDynamicUA()) {
                userAgent = userAgentPool.getUserAgent(UserAgentPool.CLIENT);
            } else {
                userAgent = requestMeta.getUserAgent();
            }
            try {
                if (requestMeta.getMethod().equals("get")) {
                    responseMeta = OkHttpTool.httpGet(requestMeta.getUrl(), userAgent, requestMeta.getProxyABLE(), proxy.getHttpHost(), proxy.getPort(), requestMeta.getHeaders(), requestMeta.getCookies(), requestMeta.getParams(), requestMeta.getEncoding());
                } else {
                    responseMeta = OkHttpTool.httpPost(requestMeta.getUrl(), userAgent, requestMeta.getProxyABLE(), proxy.getHttpHost(), proxy.getPort(), requestMeta.getHeaders(), requestMeta.getCookies(), requestMeta.getParams(), requestMeta.getEncoding());
                }
                if (responseMeta != null) {
                    proxy.setStateCode(responseMeta.getStatusCode());
                    if (responseMeta.getStatusCode() == 200) {
                        if (responseMeta.getBody().toLowerCase().contains(requestMeta.getSuccessTag())) {
                            TaskResponse response = new TaskResponse();
                            response.setCrawlerId(request.getCrawlerId());
                            response.setPaged(request.isPaged());
                            response.setResponseMeta(responseMeta);
                            response.setRequestMeta(requestMeta);
                            response.setContext(request.getContext());
                            logger.info(response.toString());
                            sender.tell(response, recipient);
                        } else {
                            request.setDelayTime(deLaytime + 20);
                            logger.warn(request.getCrawlerId() + " " + Proxy.ERROR_403 + " recall proxy -1");
                            request.setRetry(true);
                            request.incrementCurrentTryNum();
                            sender.tell(request, ActorRef.noSender());
                        }
                    } else {
                        request.setDelayTime(deLaytime + 10);
                        logger.warn(request.getCrawlerId() + " " + responseMeta.getStatusCode() + " recall proxy -2");
                        request.setRetry(true);
                        request.incrementCurrentTryNum();
                        sender.tell(request, ActorRef.noSender());
                    }
                }

            } catch (Exception e) {
                proxy.setStateCode(500);
                request.setDelayTime(deLaytime + 10);
                logger.error(request.getCrawlerId() + " " + Proxy.ERROR_500 + " server error", e.getLocalizedMessage());
                request.setRetry(true);
                request.incrementCurrentTryNum();
                sender.tell(request, ActorRef.noSender());
            } finally {
                proxyPool.returnProxy(proxy);
            }
        }
    }
}