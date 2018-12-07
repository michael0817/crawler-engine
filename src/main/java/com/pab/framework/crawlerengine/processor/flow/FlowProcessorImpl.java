package com.pab.framework.crawlerengine.processor.flow;

import com.pab.framework.crawlerdb.dao.CrawlerFlowInfoDao;
import com.pab.framework.crawlerdb.domain.CrawlerFlowInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
public class FlowProcessorImpl implements FlowProcessor{

    @Autowired
    private CrawlerFlowInfoDao crawlerFlowInfoDao;;
    @Autowired
    private  FlowDetailProcessor flowDetailProcessor;

@Override
 public void start(){
     List<CrawlerFlowInfo> crawlerFlowInfos = crawlerFlowInfoDao.findAll();

     for (CrawlerFlowInfo crawlerFlowInfo : crawlerFlowInfos) {
         flowDetailProcessor.process(crawlerFlowInfo.getFlowId());
     }
 }
}
