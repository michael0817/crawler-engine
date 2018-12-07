import com.pab.framework.crawlerengine.CrawlerEngineApplication;
import com.pab.framework.crawlerengine.task.MallTaskHandler;
import com.pab.framework.crawlerengine.task.NewsTaskHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CrawlerEngineApplication.class)
public class Application {
    @Autowired
    private NewsTaskHandler newsTaskHandler;
    @Autowired
    private MallTaskHandler mallTaskHandler;

    @Test
    public void test() {
        newsTaskHandler.taskRun();
        mallTaskHandler.taskRun();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
