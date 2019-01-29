package com.pab.framework.crawlerengine.task;

import com.pab.framework.crawlerengine.enums.FlowTypeEnum;
import com.pab.framework.crawlerengine.processor.flow.FlowProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;

/**
 * @author xumx
 * @date 2019/1/25
 */
public class CrawlerTask implements Callable<Boolean> {
    @Autowired
    private FlowProcessor flowProcessorImpl;

    @Override
    public Boolean call() throws Exception {
        flowProcessorImpl.run(FlowTypeEnum.NEWS);
        return true;
    }
}
