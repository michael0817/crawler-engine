package com.pab.framework.crawlerengine.crawler;

import com.pab.framework.crawlerdb.dao.CrawerActionDynamicInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerNewsMilestoneDao;
import com.pab.framework.crawlerdb.domain.CrawerActionDynamicInfo;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerArticle;
import com.pab.framework.crawlerdb.domain.CrawlerNewsMilestone;
import com.pab.framework.crawlerengine.processor.DetailPageProcessor;
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
public class CrawlerHandlerImpl implements CrawlerHandler {
    @Autowired
    private DetailPageProcessor detailPageProcessor;
    @Autowired
    private CrawlerNewsMilestoneDao crawlerNewsMilestoneDao;
    @Autowired
    private CrawerActionDynamicInfoDao crawerActionDynamicInfoDao;

    @Override
    public void handler(CrawlerActionInfo crawlerActionInfo,  List<String> urlAddrs) throws IOException {

        String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();
        Integer urlType = crawlerActionInfo.getUrlType();
        String urlAddr = crawlerActionInfo.getUrlAddr();
        int actionId = CrawlerUtil.extractIntOfStr(urlAddr, "[", "]");
        int actionType = crawlerActionInfo.getActionType();
        crawerActionDynamicInfoDao.deleteAll(actionId);
        int size = urlAddrs.size();
        CrawerActionDynamicInfo crawerActionDynamicInfo;
        CrawlerNewsMilestone crawlerNewsMilestone;
        List<CrawlerArticle> crawlerArticles;
        String actionDesc;
        for (int i = 0; i < size; i++) {
            crawerActionDynamicInfo = new CrawerActionDynamicInfo();
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
        actionDesc = crawlerActionInfo.getActionDesc();
        FileUtils.write(actionDesc, crawlerArticles);


    }
}
