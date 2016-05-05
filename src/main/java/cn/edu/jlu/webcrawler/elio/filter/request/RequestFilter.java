package cn.edu.jlu.webcrawler.elio.filter.request;

import cn.edu.jlu.webcrawler.elio.filter.FilterChain;
import cn.edu.jlu.webcrawler.elio.request.TaskRequest;

/**
 * @author zhuqi259
 */
public interface RequestFilter {
    void doFilter(TaskRequest req, FilterChain chain);
}
