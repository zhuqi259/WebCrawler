package cn.edu.jlu.webcrawler.elio.proxy;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuqi259
 */
@Qualifier
public class FileProxyPool extends AbstractProxyPool implements ProxyPool {
    private static class CounterLine implements LineProcessor<List<String>> {
        private List<String> lines = new ArrayList<>(1000);

        @Override
        public boolean processLine(String line) throws IOException {
            if (line != null) {
                String[] lineString = line.split("@");
                if (lineString.length > 1) {
                    lines.add(lineString[0]);
                }
            }
            return true;
        }

        @Override
        public List<String> getResult() {
            return lines;
        }
    }

    public FileProxyPool() {
    }


    @Override
    public void initProxy() {
        logger.info("FileProxyPool..........initProxy.....begin....");
        // 读取文件方式
        String testFilePath = proxyFilePath + File.separatorChar + proxyFileName;
        File testFile = new File(testFilePath);
        CounterLine counter = new CounterLine();
        try {
            List<String> proxyList = Files.readLines(testFile, Charsets.UTF_8, counter);
            addProxy(proxyList);
        } catch (IOException e) {
            logger.error("读取" + proxyFileName + " 文件失败", e);
        }
        logger.info("FileProxyPool..........initProxy.....end....");
    }
}
