package cn.edu.jlu.webcrawler.elio.parser.impl;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import cn.edu.jlu.webcrawler.elio.actor.base.BaseActor;
import cn.edu.jlu.webcrawler.elio.dao.StudentInfoMySQLMapper;
import cn.edu.jlu.webcrawler.elio.model.StudentInfo;
import cn.edu.jlu.webcrawler.elio.parser.Parser;
import cn.edu.jlu.webcrawler.elio.response.TaskResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * @author zhuqi259
 */
public class GimStudentParser extends BaseActor implements Parser<StudentInfo> {
    private Logger logger = LoggerFactory.getLogger("parse");

    @Override
    public List<StudentInfo> parse(String targetString) {
        return null;
    }

    private static final String BASE = "http://gim.jlu.edu.cn";

    private static final String storage = "E:\\student";

    private void downloadSomething(String urlString, String filename,
                                   String savePath) {
        InputStream is = null;
        OutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            // 设置请求超时为5s
            con.setConnectTimeout(5 * 1000);
            // 输入流
            is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf = new File(savePath);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            os = new FileOutputStream(sf.getPath() + File.separatorChar + filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException ignored) {
        } finally {
            // 完毕，关闭所有链接
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignored) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private StudentInfo praseHtml(String html) {
        Document doc = Jsoup.parse(html);
        Element mytable = doc.select("table.mytable").first();
        Element pic = doc.select("img[src~=(?i)\\.(png|jpe?g)]").first();
        // find the pic url...
        String link = BASE + pic.attr("src");
        Elements elements = mytable.select("tr");
        StudentInfo user = new StudentInfo();
        String txt;
        Elements tds;
        // 第一个tr
        Element tr0 = elements.get(0);
        tds = tr0.select("td");
        // 学号
        txt = tds.get(1).text();
        if (!(txt == null || txt.equals(""))) {
            user.setUserno(txt);
        } else {
            return null;
        }
        // 姓名
        txt = tds.get(3).text();
        if (!(txt == null || txt.equals(""))) {
            user.setUsername(txt);
        }
        // 性别
        txt = tds.get(5).text();
        if ("男".equals(txt)) {
            user.setGender(0);
        } else {
            user.setGender(1);
        }
        // 照片 已选
        Element tr1 = elements.get(1);
        tds = tr1.select("td");
        // 学院
        txt = tds.get(1).text();
        if (!(txt == null || txt.equals(""))) {
            user.setDepartment(txt);
        }
        // 专业
        txt = tds.get(3).text();
        //    if (!(txt == null || txt.equals(""))) {
        user.setMajor(txt);
        //     }
        // 导师
        txt = tds.get(5).text();
        //     if (!(txt == null || txt.equals(""))) {
        user.setTeacher(txt);
        //     }

        Element tr4 = elements.get(4);
        tds = tr4.select("td");
        // 手机
        txt = tds.get(3).text();
        //      if (!(txt == null || txt.equals(""))) {
        user.setTelphone(txt);
        //      }

        Element tr5 = elements.get(5);
        tds = tr5.select("td");
        // 邮箱
        txt = tds.get(3).text();
        //       if (!(txt == null || txt.equals(""))) {
        user.setEmail(txt);
        //      }

        // 下载图片
        String picurl = user.getUserno() + ".jpg";
        downloadSomething(link, picurl, storage);
        user.setPicurl(picurl);
        return user;
    }

    @Inject
    private StudentInfoMySQLMapper studentInfoMySQLMapper;

    @Override
    public void parallelProcess(ActorRef sender, Object message, ActorRef recipient, ActorContext context) {
        if (message instanceof TaskResponse) {
            TaskResponse response = (TaskResponse) message;
            String html = response.getResponseMeta().getBody();
            StudentInfo studentInfo = this.praseHtml(html);
            studentInfoMySQLMapper.insertStudent(studentInfo);
            System.out.println(studentInfo);
        }
    }
}
