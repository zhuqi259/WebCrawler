package cn.edu.jlu.webcrawler.elio.task;

import cn.edu.jlu.webcrawler.elio.request.TaskRequestMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhuqi259
 */
public class BaseTask implements Task {
    protected Logger logger = LoggerFactory.getLogger("task");
    protected String taskName;   //任务名称
    protected String taskType;  //任务类型
    protected TaskRequestMeta requestMeta;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public TaskRequestMeta getRequestMeta() {
        return requestMeta;
    }

    public void setRequestMeta(TaskRequestMeta requestMeta) {
        this.requestMeta = requestMeta;
    }
}
