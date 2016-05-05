package cn.edu.jlu.webcrawler.elio.response;

import cn.edu.jlu.webcrawler.elio.request.TaskRequestMeta;

import java.util.Map;

/**
 * @author zhuqi259
 */
public class TaskResponse implements Cloneable {
    private Map<String, String> context;  //扩展参数
    private Long crawlerId;               //抓取任务的Id
    private TaskResponseMeta responseMeta;
    private boolean paged = false;    //  当前页面是否为分页响应
    private TaskRequestMeta requestMeta;

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public Long getCrawlerId() {
        return crawlerId;
    }

    public void setCrawlerId(Long crawlerId) {
        this.crawlerId = crawlerId;
    }

    public TaskResponseMeta getResponseMeta() {
        return responseMeta;
    }

    public void setResponseMeta(TaskResponseMeta responseMeta) {
        this.responseMeta = responseMeta;
    }

    public boolean isPaged() {
        return paged;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
    }

    public TaskRequestMeta getRequestMeta() {
        return requestMeta;
    }

    public void setRequestMeta(TaskRequestMeta requestMeta) {
        this.requestMeta = requestMeta;
    }

    @Override
    public String toString() {
        return crawlerId + " res {" +
                "responseMeta=" + responseMeta +
                ", paged=" + paged +
                ", context=" + context +
                '}';
    }
}
