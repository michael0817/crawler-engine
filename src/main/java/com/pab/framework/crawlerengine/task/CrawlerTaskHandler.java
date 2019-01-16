package com.pab.framework.crawlerengine.task;

import com.pab.framework.crawlerengine.enums.FlowTypeEnum;
import com.pab.framework.crawlerengine.processor.flow.FlowProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * @author xumx
 * @date 2018/11/16
 */

@Component
@Configurable
@EnableScheduling
@Slf4j
public class CrawlerTaskHandler implements TaskHandler {
    @Autowired
    ExecutorService threadFactory;
    @Autowired
    private FlowProcessor flowProcessor;

    @Scheduled(cron = "0 17 10 * * MON-FRI")
    @Override
    public void taskRun() {
        threadFactory.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    flowProcessor.run(FlowTypeEnum.NEWS);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
