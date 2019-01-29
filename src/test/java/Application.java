import com.pab.framework.crawlerengine.CrawlerEngineApplication;
import com.pab.framework.crawlerengine.enums.FlowTypeEnum;
import com.pab.framework.crawlerengine.processor.flow.FlowProcessor;
import com.pab.framework.crawlerengine.service.PdfService;
import com.pab.framework.crawlerengine.service.ProxyService;
import com.pab.framework.crawlerengine.task.NewsTaskHandler;
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
    private FlowProcessor flowProcessorImpl;
    @Autowired
    private NewsTaskHandler newsTaskHandler;
    @Autowired
    private ProxyService proxyConfig;
    @Autowired
    private PdfService pdfService;

    @Test
    public void testNewsFlow() {
        try {
            flowProcessorImpl.run(FlowTypeEnum.NEWS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMallFlow() {
        try {
            flowProcessorImpl.run(FlowTypeEnum.MALL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTask() {
        newsTaskHandler.taskRun();
    }

    @Test
    public void testProxy(){
        proxyConfig.getNewProxyIp();
    }


    @Test
    public void testPdf(){
        pdfService.generateNewsFile();
    }
}
