package cn.edu.jlu.webcrawler.kddexample.sample;

import cn.edu.jlu.webcrawler.kddexample.task.Download;
import cn.edu.jlu.webcrawler.kddexample.task.impl.TaskDownload;
import cn.edu.jlu.webcrawler.kddexample.util.ThreadPoolManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by zhuqi259 on 2015/10/30.
 */
public class KDDParser {

    public final static String USERAGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36";

    public static void parseHtml(ThreadPoolManager pool, String base, String url, String prefix) {
        // String base = "http://dl.acm.org/";
        org.jsoup.Connection connection = null;
        try {
            // String url = "http://dl.acm.org/citation.cfm?id=2623330&preflayout=flat";
            connection = Jsoup.connect(url).header("User-Agent", USERAGENT)
                    .timeout(200 * 1000);
            Document doc = connection.get();
            // System.out.println(doc.html());
            Elements links = doc.select("a[href~=(ft_gateway.cfm)]");

            int n = links.size();
            System.out.println("***************** => " + n);
            Download[] tasks = new TaskDownload[n];
            for (int i = 0; i < n; i++) {
                tasks[i] = new TaskDownload(base + links.get(i).attr("href"), prefix);
            }
            // ThreadPoolManager pool = ThreadPoolManager.getInstance();
            pool.BatchAddTask(tasks);
            pool.completed();
            // pool.destory();
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("ERROR");
        }
    }

    public static void main(String[] args) {
        ThreadPoolManager pool = ThreadPoolManager.getInstance();
        parseHtml(pool, "http://dl.acm.org/", "http://dl.acm.org/citation.cfm?id=2623330&preflayout=flat", "F:\\研究生\\KDD\\2014");
        parseHtml(pool, "http://dl.acm.org/", "http://dl.acm.org/citation.cfm?id=2783258&preflayout=flat", "F:\\研究生\\KDD\\2015");
        pool.destory();
    }
}

