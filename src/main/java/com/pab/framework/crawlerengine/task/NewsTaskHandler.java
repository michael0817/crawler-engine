package com.pab.framework.crawlerengine.task;

import com.pab.framework.crawlerengine.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xumx
 * @date 2018/11/16
 */

@Component
@Configurable
@EnableScheduling
public class NewsTaskHandler implements TaskHandler{

    @Autowired
    ExecutorService threadFactory;

    @Scheduled(cron = "0/1 * * * * *")
    @Override
    public void taskRun(){
        threadFactory.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("123456");
            }
        });
    }

}
