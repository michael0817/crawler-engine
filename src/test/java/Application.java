import com.pab.framework.crawlerdb.dao.CrawlerFlowDetailDao;
import com.pab.framework.crawlerengine.service.ProxyService;
import com.pab.framework.crawlerengine.CrawlerEngineApplication;
import com.pab.framework.crawlerengine.processor.flow.FlowProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CrawlerEngineApplication.class)
public class Application {
    @Autowired
    private FlowProcessor flowProcessor;
    @Autowired
    private ProxyService proxyConfig;
    @Autowired
    private CrawlerFlowDetailDao crawlerFlowDetailDao;

    @Test
    public void test() {
        try {
            flowProcessor.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProxy(){
        proxyConfig.getNewProxyIp();
    }

    @Test
    public void testDb(){
        crawlerFlowDetailDao.findNextAction(1);
    }
}
