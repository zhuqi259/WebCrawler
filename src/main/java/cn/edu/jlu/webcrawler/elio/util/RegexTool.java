package cn.edu.jlu.webcrawler.elio.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhuqi259
 */
public class RegexTool {
    public static List<String> getResult(String src, String regex) {
        ArrayList<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
}
