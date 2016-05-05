package cn.edu.jlu.webcrawler.elio;

import cn.edu.jlu.webcrawler.elio.task.impl.StudentTask;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zhuqi259
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger("server");
    public static ApplicationContext context;

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("config/log4j.properties");
        context = new ClassPathXmlApplicationContext("applicationContext-main.xml");

//        ProxyTask task = (ProxyTask) context.getBean("proxy");
//        task.execute();

        StudentTask task = (StudentTask) context.getBean("student");
        task.execute();
    }
}
