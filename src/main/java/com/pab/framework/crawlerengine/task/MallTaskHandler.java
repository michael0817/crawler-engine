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
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author xumx
 * @date 2018/11/16
 */

@Component
@Configurable
@EnableScheduling
@Slf4j
public class MallTaskHandler implements TaskHandler {
    @Autowired
    ExecutorService threadService;
    @Autowired
    private FlowProcessor flowProcessor;

    @Scheduled(cron = "0 0 6 * * FRI")
    @Override
    public void taskRun() {
        try {
            flowProcessor.run(FlowTypeEnum.MALL);
            log.info("网上商城爬虫任务执行成功");
        } catch (Exception e){
            log.error("网上商城爬虫任务执行失败", e);
        }
    }

}
