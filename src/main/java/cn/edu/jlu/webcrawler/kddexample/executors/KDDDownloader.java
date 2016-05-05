package cn.edu.jlu.webcrawler.kddexample.executors;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author zhuqi259
 */
public class KDDDownloader {
    public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.93 Safari/537.36";

    private static void doWork(String base, String url, String prefix) {
        //String base = "http://dl.acm.org/";
        //String prefix = "F:/KDD2014";
        Connection connection;
        try {
            //String url = "http://dl.acm.org/citation.cfm?id=2623330&preflayout=flat";
            connection = Jsoup.connect(url).header("User-Agent", USER_AGENT)
                    .timeout(200 * 1000);
            Document doc = connection.get();
            Elements links = doc.select("a[href~=(ft_gateway.cfm)]");
            int n = links.size();
            System.out.println("***************** => " + n);
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(50);
            // 创建多个有返回值的任务
            List<Future> list = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                Callable c = new DownloaderCallable(i, base + links.get(i).attr("href"), prefix);
                // 执行任务并获取Future对象
                Future f = pool.submit(c);
                list.add(f);
            }
            pool.shutdown();
            // 获取所有并发任务的运行结果
            for (Future f : list) {
                // 从Future对象上获取任务的返回值，并输出到控制台
                System.out.println(">>>" + f.get().toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // doWork();
        doWork("http://dl.acm.org/", "http://dl.acm.org/citation.cfm?id=2623330&preflayout=flat", "F:/KDD2014");
        doWork("http://dl.acm.org/", "http://dl.acm.org/citation.cfm?id=2783258&preflayout=flat", "F:/KDD2015");
    }
}