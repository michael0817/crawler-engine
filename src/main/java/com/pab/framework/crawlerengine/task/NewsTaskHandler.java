package com.pab.framework.crawlerengine.task;

import com.pab.framework.crawlerdb.dao.*;
import com.pab.framework.crawlerdb.domain.*;
import com.pab.framework.crawlerengine.crawler.processor.DetailPageProcessor;
import com.pab.framework.crawlerengine.crawler.util.CrawlerUtil;
import com.pab.framework.crawlerengine.crawler.util.FileUtils;
import com.pab.framework.crawlerengine.processor.action.ActionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CrawlerFlowInfoDao crawlerFlowInfoDao;
    @Autowired
    private CrawlerFlowDetailDao crawlerFlowDetailDao;
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;
    @Autowired
    private CrawerActionDynamicInfoDao crawerActionDynamicInfoDao;
    @Autowired
    private CrawlerNewsMilestoneDao crawlerNewsMilestoneDao;
    @Autowired
    private ActionProcessor actionProcessor;

    @Autowired
    private DetailPageProcessor detailPageProcessor;

    //@Scheduled(cron = "0  0 8 * * ?")
    @Override
    public void taskRun() {
        threadFactory.execute(new Runnable() {
            @Override
            public void run() {
                List<CrawlerFlowInfo> crawlerFlowInfos = crawlerFlowInfoDao.findAll();
                List<CrawlerFlowDetail> crawlerFlowDetails;
                CrawlerActionInfo crawlerActionInfo;
                List<CrawlerArticle> crawlerArticles;
                String urlAddr;
                Integer urlType;
                List<String> urlAddrs=null;
                String baseUrlAddr;
                CrawerActionDynamicInfo crawerActionDynamicInfo;
                CrawlerNewsMilestone crawlerNewsMilestone;

                String actionDesc;

                for (CrawlerFlowInfo crawlerFlowInfo : crawlerFlowInfos) {
                    crawlerFlowDetails = crawlerFlowDetailDao.findAllByFlowId(crawlerFlowInfo.getFlowId());
                    for (CrawlerFlowDetail crawlerFlowDetail : crawlerFlowDetails) {
                        crawlerActionInfo = crawlerActionInfoDao.findOneByActionById(crawlerFlowDetail.getActionId());
                        int actionType = crawlerActionInfo.getActionType();

                        switch (actionType){
                            case 2:
                              urlAddrs=  actionProcessor.getUrlAddrs(crawlerActionInfo);
                              break;
                            case 3:
                                baseUrlAddr=crawlerActionInfo.getBaseUrlAddr();
                                urlType=crawlerActionInfo.getUrlType();
                                urlAddr=crawlerActionInfo.getUrlAddr();
                                crawlerActionInfo.getUrlType();
                                int actionId = CrawlerUtil.extractIntOfStr(urlAddr, "[", "]");
                                crawerActionDynamicInfoDao.deleteAll(actionId);
                                int size=urlAddrs.size();
                                for (int i = 0; i < size; i++) {
                                    crawerActionDynamicInfo=new CrawerActionDynamicInfo();
                                    crawerActionDynamicInfo.setActionId(actionId);
                                    crawerActionDynamicInfo.setActionType(actionType);
                                    crawerActionDynamicInfo.setUrlType(urlType);
                                    crawerActionDynamicInfo.setUrlAddr(urlAddrs.get(i));
                                    crawerActionDynamicInfoDao.insertAll(crawerActionDynamicInfo);
                                    crawlerNewsMilestone = new CrawlerNewsMilestone();
                                    crawlerNewsMilestone.setActionId(actionId);
                                    crawlerNewsMilestone.setUrlAddr(urlAddrs.get(i));
                                    int existsUrl = crawlerNewsMilestoneDao.isExistsUrl(crawlerNewsMilestone);
                                    if (existsUrl == 0) {
                                        crawlerNewsMilestoneDao.insertAll(crawlerNewsMilestone);
                                    }
                                }
                                urlAddrs = crawlerNewsMilestoneDao.findUrlAddrsByNewDate(actionId);
                                crawlerArticles = detailPageProcessor.process(baseUrlAddr, urlAddrs);
                                actionDesc=crawlerActionInfo.getActionDesc();
                                try {
                                    FileUtils.write(actionDesc, crawlerArticles);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                    }
                }
            }
        });
    }

}
