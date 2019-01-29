package com.pab.framework.crawlerengine.task;

import com.pab.framework.crawlerengine.enums.FlowTypeEnum;
import com.pab.framework.crawlerengine.processor.flow.FlowProcessor;
import com.pab.framework.crawlerengine.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author xumx
 * @date 2018/11/16
 */

@Component
@Configurable
@EnableScheduling
@Slf4j
public class NewsTaskHandler implements TaskHandler {
    @Autowired
    ExecutorService threadService;
    @Autowired
    PdfService pdfService;
    @Autowired
    private FlowProcessor flowProcessorImpl;

    @Scheduled(cron = "0 0 8 * * MON-FRI")
    @Override
    public void taskRun() {
        try {
            CrawlerTask task = new CrawlerTask();
            FutureTask<Boolean> futureTask = new FutureTask(task);
            threadService.submit(futureTask);
            threadService.shutdown();
            if(futureTask.get(60,TimeUnit.MINUTES)){
                pdfService.generateNewsFile();
                log.info("新闻爬虫任务执行成功");
            }
        } catch (Exception e){
            log.error("新闻爬虫任务执行失败", e);
        }
    }

}
