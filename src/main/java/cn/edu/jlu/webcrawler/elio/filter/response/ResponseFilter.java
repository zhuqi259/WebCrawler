package cn.edu.jlu.webcrawler.elio.filter.response;

import cn.edu.jlu.webcrawler.elio.filter.FilterChain;
import cn.edu.jlu.webcrawler.elio.response.TaskResponse;

/**
 * @author zhuqi259
 */
public interface ResponseFilter {
    void doFilter(TaskResponse resp, FilterChain chain);
}
