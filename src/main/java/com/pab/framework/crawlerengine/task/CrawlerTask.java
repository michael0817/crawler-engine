package com.pab.framework.crawlerengine.task;

import com.pab.framework.crawlerengine.enums.FlowTypeEnum;
import com.pab.framework.crawlerengine.processor.flow.FlowProcessor;
import com.sun.tools.javac.comp.Flow;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;

/**
 * @author xumx
 * @date 2019/1/25
 */
public class CrawlerTask implements Callable<Boolean> {

    private static FlowTypeEnum flowType;

    public CrawlerTask(){}

    public CrawlerTask(FlowTypeEnum flowType){
        this.flowType = flowType;
    }
    @Autowired
    private FlowProcessor flowProcessorImpl;

    @Override
    public Boolean call() throws Exception {
        if(flowType != null){
            flowProcessorImpl.run(flowType);
        }
        return true;
    }
}
