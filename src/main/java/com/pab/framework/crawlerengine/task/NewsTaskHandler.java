package com.pab.framework.crawlerengine.task;

import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerFlowDetailDao;
import com.pab.framework.crawlerdb.dao.CrawlerFlowInfoDao;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;
import com.pab.framework.crawlerdb.domain.CrawlerFlowInfo;
import com.pab.framework.crawlerengine.processor.action.ActionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author xumx
 * @date 2018/11/16
 */

@Component
@Configurable
@EnableScheduling
public class NewsTaskHandler implements TaskHandler {
    @Autowired
    ExecutorService threadFactory;
    @Autowired
    private CrawlerFlowDetailDao crawlerFlowDetailDao;
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;
    @Autowired
    private CrawlerFlowInfoDao crawlerFlowInfoDao;
    @Autowired
    private ActionProcessor actionProcessor;

    //@Scheduled(cron = "0/1 * * * * *")
    @Override
    public void taskRun() {
        threadFactory.execute(new Runnable() {
            @Override
            public void run() {
                List<CrawlerFlowInfo> crawlerFlowInfos=crawlerFlowInfoDao.findAll();
                List<CrawlerFlowDetail> crawlerFlowDetails;
                CrawlerActionInfo crawlerActionInfo;
                for (CrawlerFlowInfo crawlerFlowInfo : crawlerFlowInfos) {
                    int flowId=crawlerFlowInfo.getFlowId();
                    crawlerFlowDetails = crawlerFlowDetailDao.findAllByFlowId( flowId );
                    for (CrawlerFlowDetail crawlerFlowDetail : crawlerFlowDetails){
                        int actionId = crawlerFlowDetail.getActionId();
                        crawlerActionInfo = actionProcessor.findOneByActionById( actionId );
                        try {
                            actionProcessor.processor( crawlerActionInfo );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

}
