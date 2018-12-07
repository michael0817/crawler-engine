package com.pab.framework.crawlerengine.processor.flow;

import com.pab.framework.crawlerdb.dao.*;
import com.pab.framework.crawlerdb.domain.*;
import com.pab.framework.crawlerengine.processor.DetailPageProcessor;
import com.pab.framework.crawlerengine.processor.action.ActionProcessor;
import com.pab.framework.crawlerengine.util.CrawlerUtil;
import com.pab.framework.crawlerengine.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
public class FlowProcessorImpl implements FlowProcessor{

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

@Override
 public void start(){
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
}
