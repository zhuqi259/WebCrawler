package cn.edu.jlu.webcrawler.elio.response;

import cn.edu.jlu.webcrawler.elio.util.HtmlCompressor;

import java.util.List;
import java.util.Map;

/**
 * @author zhuqi259
 */
public class TaskResponseMeta {
    private List<Map<String, String>> cookies;
    private Map<String, List<String>> header;
    private int statusCode;
    private String url;
    private String body;
    private boolean isLogBody = false; //显示html日志  方便调试,生产环境不建议使用

    public List<Map<String, String>> getCookies() {
        return cookies;
    }

    public void setCookies(List<Map<String, String>> cookies) {
        this.cookies = cookies;
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public void setHeader(Map<String, List<String>> header) {
        this.header = header;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isLogBody() {
        return isLogBody;
    }

    public void setLogBody(boolean logBody) {
        isLogBody = logBody;
    }

    @Override
    public String toString() {
        return "{" +
                "url='" + url + '\'' +
                ", cookies=" + cookies +
                ", header=" + header +
                ", statusCode=" + statusCode +
                ", body=" + (isLogBody ? HtmlCompressor.compress(body) : "") +
                // ", body=" +HtmlCompressor.compress(body) +
                '}';
    }
}
