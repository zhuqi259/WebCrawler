package cn.edu.jlu.webcrawler.elio.parser;

import java.util.List;

/**
 * @param <T>
 * @author zhuqi259
 */
public interface Parser<T> {
    List<T> parse(String targetString);
}
