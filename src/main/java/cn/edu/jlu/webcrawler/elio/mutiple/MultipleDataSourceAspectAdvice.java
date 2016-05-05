package cn.edu.jlu.webcrawler.elio.mutiple;

import cn.edu.jlu.webcrawler.elio.dao.ProxyInfoMySQLMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author zhuqi259
 */
@Component
@Aspect
public class MultipleDataSourceAspectAdvice {
    private static Logger logger = LoggerFactory.getLogger(MultipleDataSourceAspectAdvice.class);

    @Around("execution(* cn.edu.jlu.webcrawler.elio.business.sqlmapper.*.*(..))")
    public Object doAround(ProceedingJoinPoint jp) throws Throwable {
        if (jp.getTarget() instanceof ProxyInfoMySQLMapper) {
            MultipleDataSource.setDataSourceKey("mySqlDataSource");
        }
        return jp.proceed();
    }
}