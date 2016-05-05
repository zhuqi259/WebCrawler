package cn.edu.jlu.webcrawler.elio.request;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhuqi259
 */
public class TaskRequest {
    private TaskRequestMeta requestMeta;
    private String proxyHost;    //代理 主机
    private int proxyPort;     // 代理端口
    private Map<String, String> context;  //扩展参数
    private Long crawlerId;               //抓取任务的ID
    private int maxReTryNum = 50;     //最大失败次数
    private AtomicInteger currentTryNum = new AtomicInteger();   // 当前重试次数
    private int delayTime;        // 延时请求发送时间 s为单位
    private boolean discard = false;   //是否丢弃
    private boolean retry = false;
    private boolean fixUserAgent;
    /**
     * 是否同步  当前使用同步抓取，可以修改OkHttpTool  使用异步抓取
     */
    private boolean sync = false;
    private boolean paged = false;

    public TaskRequestMeta getRequestMeta() {
        return requestMeta;
    }

    public void setRequestMeta(TaskRequestMeta requestMeta) {
        this.requestMeta = requestMeta;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

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

    public int getMaxReTryNum() {
        return maxReTryNum;
    }

    public void setMaxReTryNum(int maxReTryNum) {
        this.maxReTryNum = maxReTryNum;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public boolean isDiscard() {
        return discard;
    }

    public void setDiscard(boolean discard) {
        this.discard = discard;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    public boolean isFixUserAgent() {
        return fixUserAgent;
    }

    public void setFixUserAgent(boolean fixUserAgent) {
        this.fixUserAgent = fixUserAgent;
    }

    public boolean isPaged() {
        return paged;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    @Override
    public String toString() {
        return crawlerId + " req {" +
                "requestMeta =" + requestMeta +
                ", currentTryNum=" + currentTryNum.intValue() +
                ", maxReTryNum=" + maxReTryNum +
                ", delayTime=" + delayTime +
                ", fixUserAgent=" + fixUserAgent +
                ", sync=" + sync +
                ", retry=" + retry +
                ", paged=" + paged +
                ", context=" + context +
                ", proxy=[" + proxyHost + ":" + proxyPort + "]" +
                '}';
    }

    public void incrementCurrentTryNum() {
        currentTryNum.getAndIncrement();
    }

    public int getCurrentTryNum() {
        return currentTryNum.get();
    }
}
