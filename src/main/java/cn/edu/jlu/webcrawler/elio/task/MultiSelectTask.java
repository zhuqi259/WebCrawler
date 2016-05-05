package cn.edu.jlu.webcrawler.elio.task;

import cn.edu.jlu.webcrawler.elio.request.TaskRequestMeta;

import java.util.List;

/**
 * @author zhuqi259
 */
public abstract class MultiSelectTask extends BaseTask {
    protected List<TaskRequestMeta> requestMetaList;

    public List<TaskRequestMeta> getRequestMetaList() {
        return requestMetaList;
    }

    public void setRequestMetaList(List<TaskRequestMeta> requestMetaList) {
        this.requestMetaList = requestMetaList;
    }
}
