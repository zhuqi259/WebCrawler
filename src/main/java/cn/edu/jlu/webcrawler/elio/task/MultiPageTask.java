package cn.edu.jlu.webcrawler.elio.task;

/**
 * @author zhuqi259
 */
public abstract class MultiPageTask extends BaseTask {
    protected String pageParam;       //分页参数
    protected boolean isPaged = false;
    protected boolean isFixPageSize = false;
    protected long delay = 10;     //延时
    protected long period = 10;   //运行周期  s

    public String getPageParam() {
        return pageParam;
    }

    public void setPageParam(String pageParam) {
        this.pageParam = pageParam;
    }

    public boolean isPaged() {
        return isPaged;
    }

    public void setPaged(boolean paged) {
        isPaged = paged;
    }

    public boolean isFixPageSize() {
        return isFixPageSize;
    }

    public void setFixPageSize(boolean fixPageSize) {
        isFixPageSize = fixPageSize;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}
