package cn.edu.jlu.webcrawler.kddexample.task;

/**
 * Created by zhuqi259 on 2015/10/30.
 */
public abstract class Download {
    protected static final int RETRY_TIME = 3;
    protected String info;
    protected String prefix;

    public abstract boolean downloadFile();

    public String getInfo() {
        return info;
    }
}
