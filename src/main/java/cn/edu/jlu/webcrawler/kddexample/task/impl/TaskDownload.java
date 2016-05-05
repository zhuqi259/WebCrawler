package cn.edu.jlu.webcrawler.kddexample.task.impl;

import cn.edu.jlu.webcrawler.kddexample.sample.KDDParser;
import cn.edu.jlu.webcrawler.kddexample.task.Download;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhuqi259 on 2015/10/30.
 */

public class TaskDownload extends Download {

    public TaskDownload(String info, String prefix) {
        this.info = info;
        this.prefix = prefix;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public boolean downloadFile() {
        System.out.println(info + " begin ....");
        downloadSomething(info, "*.pdf", prefix);
        System.out.println(info + " end ....");
        return false;
    }

    private static void downloadSomething(String urlString, String filename,
                                          String savePath) {
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection conn = null;
        int time = 0;
        do {
            try {
                // 构造URL
                URL url = new URL(urlString);
                // 打开连接
                conn = (HttpURLConnection) url.openConnection();
                // 设置 User-Agent
                conn.setRequestProperty("User-Agent", KDDParser.USERAGENT);
                // 设置请求超时为60s
                conn.setConnectTimeout(60 * 1000);
                // 特别重要...
                int responseCode = conn.getResponseCode();
                System.out.println(responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // http://delivery.acm.org/10.1145/2640000/2630816/p1-etzioni.pdf?ip=59.72.109.114&id=2630816&acc=ACTIVE%20SERVICE&key=BF85BBA5741FDC6E%2EC01CA9BA055CFEA8%2E4D4702B0C3E38B35%2E4D4702B0C3E38B35&CFID=414284820&CFTOKEN=14761593&__acm__=1409406799_d23f35ea5d160c7e1816379be27389d1
                    String realUrl = conn.getURL().toString();
                    System.out.println(realUrl);

                    // String regex = "(\\w|-)*.(pdf|mp4)";
                    String regex = "(\\w|-)*.pdf";
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(realUrl);
                    if (m.find()) { // 判断pdf
                        filename = m.group(0);
                        System.out.println(filename);
                        // 输入流
                        is = conn.getInputStream();
                        // 1K的数据缓冲
                        byte[] bs = new byte[1024];
                        // 读取到的数据长度
                        int len;
                        // 输出的文件流
                        File sf = new File(savePath);
                        if (!sf.exists()) {
                            sf.mkdirs();
                        }
                        os = new FileOutputStream(sf.getPath() + "\\" + filename);
                        // 开始读取
                        while ((len = is.read(bs)) != -1) {
                            os.write(bs, 0, len);
                        }
                    }
                    break;
                }else{
                    time++;
                }
            } catch (IOException e) {
                time++;
                System.out.println(e);
            } finally {
                // 完毕，关闭所有链接
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                    os = null;
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                    is = null;
                }
            }
        } while (time < RETRY_TIME);
    }
}

