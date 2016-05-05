import cn.edu.jlu.webcrawler.elio.dao.ProxyInfoMySQLMapper;
import cn.edu.jlu.webcrawler.elio.model.ProxyInfo;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * @author zhuqi259
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext-main.xml")
@Transactional
public class ProxyInfoTest {
    @Inject
    private ProxyInfoMySQLMapper proxyInfoMySQLMapper;

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

    @Rollback(true)
    @org.junit.Test
    public void testInsertProxyInfo() {
        ProxyInfo proxyInfo = new ProxyInfo();
        proxyInfo.setProxyIp("192.168.1.1");
        proxyInfo.setProxyPort(80);
        proxyInfoMySQLMapper.insertProxy(proxyInfo);
    }

}
