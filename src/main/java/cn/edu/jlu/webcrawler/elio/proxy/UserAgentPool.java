package cn.edu.jlu.webcrawler.elio.proxy;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zhuqi259
 */
public class UserAgentPool {
    private final static Multimap<String, String> userAgentMultimap = ArrayListMultimap.create();
    private final static String userAgentsPath = "config";
    private final static String clientUserAgentsFileName = "clientUA.txt";
    private final static String mobileUserAgentsFileName = "mobileUA.txt";

    private static int clientUserAgentSize = 0;
    private static int mobileUserAgentSize = 0;
    public final static String CLIENT = "client";
    public final static String MOBILE = "mobile";

    private final static Logger errorLogger = LoggerFactory.getLogger("error");
    private final static Logger businessLogger = LoggerFactory.getLogger("business");

    static class CounterLine implements LineProcessor<List<String>> {
        private List<String> lines = new ArrayList<>(1000);

        @Override
        public boolean processLine(String line) throws IOException {
            if (!line.startsWith("#") && !Strings.isNullOrEmpty(line)) {
                lines.add(line);
            }
            return true;
        }

        @Override
        public List<String> getResult() {
            return lines;
        }
    }

    public UserAgentPool() {
        //  initUserAgentPool();
    }

    public void initUserAgentPool() {
        String testFilePath = userAgentsPath + File.separatorChar + clientUserAgentsFileName;
        File testFile = new File(testFilePath);
        CounterLine counter = new CounterLine();
        try {
            List<String> clientUserAgents = Files.readLines(testFile, Charsets.UTF_8, counter);
            clientUserAgentSize = clientUserAgents.size();
            businessLogger.info("加载 client userAgent  数量:" + clientUserAgentSize);
            userAgentMultimap.putAll(CLIENT, clientUserAgents);
        } catch (IOException e) {
            errorLogger.error("读取" + clientUserAgentsFileName + " 文件失败", e);
        }
        testFilePath = userAgentsPath + File.separatorChar + mobileUserAgentsFileName;
        testFile = new File(testFilePath);
        try {
            List<String> mobileUserAgents = Files.readLines(testFile, Charsets.UTF_8, counter);
            mobileUserAgentSize = mobileUserAgents.size();
            businessLogger.info("加载 mobile userAgent  数量:" + mobileUserAgentSize);
            userAgentMultimap.putAll(MOBILE, mobileUserAgents);
        } catch (IOException e) {
            errorLogger.error("读取" + mobileUserAgentsFileName + " 文件失败", e);
        }
    }

    /**
     * 随机获取UserAgent
     *
     * @param type mobile/client/all
     * @return UserAgent
     */
    public String getUserAgent(String type) {
        switch (type) {
            case CLIENT: {
                List<String> result = (List<String>) userAgentMultimap.get(CLIENT);
                return result.get(ThreadLocalRandom.current().nextInt(clientUserAgentSize));
            }
            case MOBILE: {
                List<String> result = (List<String>) userAgentMultimap.get(MOBILE);
                return result.get(ThreadLocalRandom.current().nextInt(mobileUserAgentSize));
            }
            default:
                int x = ThreadLocalRandom.current().nextInt(0, 2);
                if (x == 0) {
                    List<String> result = (List<String>) userAgentMultimap.get(CLIENT);
                    return result.get(ThreadLocalRandom.current().nextInt(clientUserAgentSize));
                } else {
                    List<String> result = (List<String>) userAgentMultimap.get(MOBILE);
                    return result.get(ThreadLocalRandom.current().nextInt(mobileUserAgentSize));
                }
        }
    }
}
