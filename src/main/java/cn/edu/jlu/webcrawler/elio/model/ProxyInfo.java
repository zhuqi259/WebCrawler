package cn.edu.jlu.webcrawler.elio.model;

/**
 * @author zhuqi259
 */
public class ProxyInfo {
    private String proxyIp;
    private int proxyPort;
    private String proxyType;
    private String proxyPosition;
    private String inter;    //  国内 0  国际 1
    private String checkTime;
    private String priority; // 0 ，1 ，2

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public String getProxyPosition() {
        return proxyPosition;
    }

    public void setProxyPosition(String proxyPosition) {
        this.proxyPosition = proxyPosition;
    }

    public String getInter() {
        return inter;
    }

    public void setInter(String inter) {
        this.inter = inter;
    }


    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "{" +
                "proxyIp='" + proxyIp + '\'' +
                ", proxyPort=" + proxyPort +
                ", proxyType='" + proxyType + '\'' +
                ", proxyPosition='" + proxyPosition + '\'' +
                ", inter='" + inter + '\'' +
                ", checkTime='" + checkTime + '\'' +
                '}';
    }
}
