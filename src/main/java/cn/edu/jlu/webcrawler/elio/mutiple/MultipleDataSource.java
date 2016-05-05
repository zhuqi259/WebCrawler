package cn.edu.jlu.webcrawler.elio.mutiple;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author zhuqi259
 */
public class MultipleDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<>();

    public static void setDataSourceKey(String dataSource) {
        dataSourceKey.set(dataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }
}