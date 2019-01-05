package com.pab.framework.crawlerengine.processor.flow;

import com.pab.framework.constant.Global;
import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerFlowDetailDao;
import com.pab.framework.crawlerdb.dao.CrawlerFlowInfoDao;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;
import com.pab.framework.crawlerdb.domain.CrawlerFlowInfo;
import com.pab.framework.crawlerengine.processor.action.ActionProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
@Slf4j
public class FlowProcessorImpl implements FlowProcessor{

    @Autowired
    private CrawlerFlowInfoDao crawlerFlowInfoDao;
    @Autowired
    private CrawlerFlowDetailDao crawlerFlowDetailDao;
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;
    @Autowired
    private ActionProcessor actionProcessorImpl;

    @Override
    public void run() {
        List<CrawlerFlowInfo> cfis = crawlerFlowInfoDao.findAll();
        for (CrawlerFlowInfo cfi : cfis) {
            if(Global.CRAWLER_OFF.equalsIgnoreCase(cfi.getFlowSchedule())){
                continue;
            }
            boolean result = false;
            List<CrawlerFlowDetail> cfds = crawlerFlowDetailDao.findAllByFlowId(cfi.getFlowId());
            for(CrawlerFlowDetail cfd : cfds){
                CrawlerActionInfo cai = crawlerActionInfoDao.findCrawlerActionInfo(cfd.getActionId());
                result = actionProcessorImpl.process(cai);
                if(result == false){
                    //TODO add fail log
                    //TODO set retry time
                }
            }
            if(result == true){
                //TODO add success log
            }
        }
    }
}
