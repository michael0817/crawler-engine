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
    private FlowProcessor flowProcessor;
    @Autowired
    PdfService pdfService;

    @Scheduled(cron = "0 0 8 * * MON-FRI")
    @Override
    public void taskRun() {
        try {
            flowProcessor.run(FlowTypeEnum.NEWS);
            pdfService.generateNewsFile();
            log.info("新闻爬虫任务执行成功");
        } catch (Exception e){
            log.error("新闻爬虫任务执行失败", e);
        }
    }

}
