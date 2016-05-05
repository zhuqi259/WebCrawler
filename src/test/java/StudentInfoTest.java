import cn.edu.jlu.webcrawler.elio.dao.ProxyInfoMySQLMapper;
import cn.edu.jlu.webcrawler.elio.dao.StudentInfoMySQLMapper;
import cn.edu.jlu.webcrawler.elio.model.ProxyInfo;
import cn.edu.jlu.webcrawler.elio.model.StudentInfo;
import cn.edu.jlu.webcrawler.elio.parser.impl.GimStudentParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author zhuqi259
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext-main.xml")
@Transactional
public class StudentInfoTest {
    @Inject
    private StudentInfoMySQLMapper studentInfoMySQLMapper;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

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
        String link = "http://gim.jlu.edu.cn"+ pic.attr("src");
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
        if (!(txt == null || txt.equals(""))) {
            user.setMajor(txt);
        }
        // 导师
        txt = tds.get(5).text();
        if (!(txt == null || txt.equals(""))) {
            user.setTeacher(txt);
        }

        Element tr4 = elements.get(4);
        tds = tr4.select("td");
        // 手机
        txt = tds.get(3).text();
        if (!(txt == null || txt.equals(""))) {
            user.setTelphone(txt);
        }

        Element tr5 = elements.get(5);
        tds = tr5.select("td");
        // 邮箱
        txt = tds.get(3).text();
        if (!(txt == null || txt.equals(""))) {
            user.setEmail(txt);
        }

        // 下载图片
        String picurl = user.getUserno() + ".jpg";
        downloadSomething(link, picurl, "D:\\student\\");
        user.setPicurl(picurl);
        return user;
    }

    @Rollback(false)
    @org.junit.Test
    public void testInsertProxyInfo() throws IOException {
        String html = "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\" />\n" +
                "<title>&nbsp;</title>\n" +
                "<style type=\"text/css\">\n" +
                "<!--\n" +
                "body{font-size: 14px;}\n" +
                ".mytitle {\n" +
                "\tfont-size: 18px;\n" +
                "\tfont-weight: bold;\n" +
                "\ttext-align:center;\n" +
                "\tmargin-bottom:20px;\n" +
                "}\n" +
                ".mytable{\n" +
                "border-collapse:collapse;\n" +
                "border:2px solid #000000;\n" +
                "\n" +
                "}\n" +
                "td{\n" +
                "\tpadding-top:5px;\n" +
                "\tpadding-bottom:5px;\n" +
                "\tword-break:break-all;\n" +
                "}\n" +
                ".qianzi{padding-right:20px;}\n" +
                ".STYLE1 {padding-right: 20px; font-weight: bold; }\n" +
                "-->\n" +
                "</style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"mytitle\">吉林大学研究生“三助”岗位审批表</div>\n" +
                "\n" +
                "<table class=\"mytable\" width=\"610\" border=\"1\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#000000\">\n" +
                "  <tr>\n" +
                "    <td width=\"93\" height=\"30\"><div align=\"center\"><strong>学号</strong></div></td>\n" +
                "    <td colspan=\"2\"><div align=\"center\">2013532067</div></td>\n" +
                "    <td width=\"53\"><div align=\"center\"><strong>姓名</strong></div></td>\n" +
                "    <td width=\"121\"><div align=\"center\">朱琪</div></td>\n" +
                "    <td width=\"53\"><div align=\"center\"><strong>性别</strong></div></td>\n" +
                "    <td width=\"57\"><div align=\"center\">男</div></td>\n" +
                "    <td width=\"113\" rowspan=\"4\" align=\"center\"><div align=\"center\"><img width=\"110\" src=\"/zs/zhaopian_sanzhu/2013532067.jpg\"></div></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\"><div align=\"center\"><strong>院（所）</strong></div></td>\n" +
                "    <td colspan=\"2\"><div align=\"center\">计算机科学与技术学院</div></td>\n" +
                "    <td><div align=\"center\"><strong>专业</strong></div></td>\n" +
                "    <td><div align=\"center\">计算机软件与理论</div></td>\n" +
                "    <td><div align=\"center\"><strong>导师</strong></div></td>\n" +
                "    <td><div align=\"center\">董立岩</div></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\" colspan=\"3\"><div align=\"center\"><strong>聘用单位/聘用者</strong></div></td>\n" +
                "    <td colspan=\"2\"><div align=\"center\">计算机科学与技术学院</div></td>\n" +
                "    <td><div align=\"center\"><strong>博硕</strong></div></td>\n" +
                "    <td><div align=\"center\">硕士</div></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\" colspan=\"2\"><div align=\"center\"><strong>聘用岗位类别</strong></div></td>\n" +
                "    <td colspan=\"5\">\n" +
                "\t<div align=\"center\">\n" +
                "\t<input type=\"checkbox\" onclick=\"return false;\" >助教　　\n" +
                "          <input type=\"checkbox\" onclick=\"return false;\" >助管　　\n" +
                "          <input type=\"checkbox\" onclick=\"return false;\" checked>助研\n" +
                "\t</div>\n" +
                "\t</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\" colspan=\"2\"><div align=\"center\"><strong>申请岗位</strong></div></td>\n" +
                "    <td colspan=\"3\"><div align=\"center\">助研</div></td>\n" +
                "    <td colspan=\"2\"><div align=\"center\"><strong>联系电话</strong></div></td>\n" +
                "    <td><div align=\"center\">15143089558</div></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\" colspan=\"2\"><div align=\"center\"><strong>聘用时间</strong></div></td>\n" +
                "    <td colspan=\"3\"><div align=\"center\">2014年9月至2015年7月</div></td>\n" +
                "    <td colspan=\"2\"><div align=\"center\"><strong>E-mail</strong></div></td>\n" +
                "    <td><div align=\"center\">zhu.qi.memory@gmail.com</div></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"100\"><div align=\"center\"><strong>申请<br />\n" +
                "    理由</strong></div></td>\n" +
                "    <td colspan=\"7\" align=\"right\" valign=\"bottom\"><div class=\"STYLE1\">研究生签名：　　　　　　　　年　　月　　日</div></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"100\"><div align=\"center\"><strong>聘用单位<br />\n" +
                "    或聘用者<br />审批意见</strong></div></td>\n" +
                "    <td colspan=\"7\" align=\"right\" valign=\"bottom\"><div class=\"STYLE1\">负责人签字（盖公章）：　　　　　　　　年　　月　　日</div></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"100\"><div align=\"center\"><strong>导师<br />意见</strong></div></td>\n" +
                "    <td colspan=\"7\" align=\"right\" valign=\"bottom\"><div class=\"STYLE1\">签名：　　　　　　　　年　　月　　日</div></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"100\"><div align=\"center\"><strong>所在<br />培养单位<br />审批意见</strong></div></td>\n" +
                "    <td colspan=\"7\" align=\"right\" valign=\"bottom\"><div class=\"STYLE1\">负责人签字（盖公章）：　　　　　　　　年　　月　　日</div></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"100\"><div align=\"center\"><strong>研究生院<br />审批意见</strong></div></td>\n" +
                "    <td colspan=\"7\" align=\"right\" valign=\"bottom\"><div class=\"STYLE1\">负责人签字（盖公章）：　　　　　　　　年　　月　　日</div></td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "<div style=\"margin:10px auto; width:500px;\">\n" +
                "注：1.“助教”、“助管”表格一式二份，一份交设岗单位，一份交研究生院。<br />\n" +
                "　　2.“助研”表格一份交所在培养单位留存，不需研究生院审批。\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        StudentInfo studentInfo = praseHtml(html);
        studentInfoMySQLMapper.insertStudent(studentInfo);
        System.out.println(studentInfo);
    }

}
