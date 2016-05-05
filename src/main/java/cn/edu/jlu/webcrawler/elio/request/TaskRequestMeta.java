package cn.edu.jlu.webcrawler.elio.request;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuqi259
 */
public class TaskRequestMeta implements Cloneable {
    private String url;             //请求URL
    private String method;          //请求  方法  get|post
    private String encoding;       //编码
    private String endMark;       //报文结束标记
    private String successTag;   //报文成功标志
    private String failTag;     //报文失败标志
    private String taskType;     // 任务类型   1 单一请求, 2 分页请求, 3 主从请求,4 动态条件请求

    private String userAgent;    //固定userAgent  getIsDynamicUA =fasle 有效
    private boolean isDynamicUA = true; // 启用动态UserAgent
    private boolean proxyABLE = true; // 默认启用代理

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> cookies = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    @Override
    public TaskRequestMeta clone() {
        TaskRequestMeta meta = null;
        try {
            meta = (TaskRequestMeta) super.clone();
            Map<String, String> copyHeaders = new HashMap<>();
            copyHeaders.putAll(headers);
            Map<String, String> copyCookies = new HashMap<>();
            copyCookies.putAll(cookies);
            Map<String, String> copyParams = new HashMap<>();
            copyParams.putAll(params);
            meta.setHeaders(copyHeaders);
            meta.setCookies(copyCookies);
            meta.setParams(copyParams);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return meta;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEndMark() {
        return endMark;
    }

    public void setEndMark(String endMark) {
        this.endMark = endMark;
    }

    public String getSuccessTag() {
        return successTag;
    }

    public void setSuccessTag(String successTag) {
        this.successTag = successTag;
    }

    public String getFailTag() {
        return failTag;
    }

    public void setFailTag(String failTag) {
        this.failTag = failTag;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean getIsDynamicUA() {
        return isDynamicUA;
    }

    public void setIsDynamicUA(boolean dynamicUA) {
        isDynamicUA = dynamicUA;
    }

    public boolean getProxyABLE() {
        return proxyABLE;
    }

    public void setProxyABLE(boolean proxyABLE) {
        this.proxyABLE = proxyABLE;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }


    @Override
    public String toString() {
        return "TaskRequestMeta{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", encoding='" + encoding + '\'' +
                ", endMark='" + endMark + '\'' +
                ", successTag='" + successTag + '\'' +
                ", failTag='" + failTag + '\'' +
                ", taskType='" + taskType + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", isDynamicUA=" + isDynamicUA +
                ", proxyABLE=" + proxyABLE +
                ", headers=" + headers +
                ", cookies=" + cookies +
                ", params=" + params +
                '}';
    }
}
