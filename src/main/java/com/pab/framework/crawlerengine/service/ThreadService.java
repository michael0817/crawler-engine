package com.pab.framework.crawlerengine.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xumx
 * @date 2018/11/19
 */
@Service
public class ThreadService {

    @Value("${threads.corePoolSize}")
    private int corePoolSize;

    @Value("${threads.maximumPoolSize}")
    private int maximumPoolSize;

    @Value("${threads.keepAliveTime}")
    private long keepAliveTime;

    @Value("${threads.blockingQueueSize}")
    private int blockingQueueSize;

    @Bean(name="threadFactory")
    public ExecutorService getExecutorService(){
        return new ThreadPoolExecutor(
                this.corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(blockingQueueSize));
    }




}
