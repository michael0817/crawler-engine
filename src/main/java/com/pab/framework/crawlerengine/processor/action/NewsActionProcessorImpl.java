package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.dao.CrawerActionDynamicInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerNewsMilestoneDao;
import com.pab.framework.crawlerdb.domain.CrawerActionDynamicInfo;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerNewsMilestone;
import com.pab.framework.crawlerengine.crawler.processor.CbrcDetailPageProcessor;
import com.pab.framework.crawlerengine.crawler.processor.DetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.crawler.util.CrawlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
public class NewsActionProcessorImpl implements ActionProcessor{
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;
    @Autowired
    private DetailUrlsPageProcessor detailUrlsPageProcessor;
    @Autowired
    private CbrcDetailPageProcessor cbrcDetailPageProcessor;
    @Autowired
    private CrawerActionDynamicInfoDao crawerActionDynamicInfoDao;
    @Autowired
    private CrawlerNewsMilestoneDao crawlerNewsMilestoneDao;

    @Override
    public void processor(CrawlerActionInfo crawlerActionInfo) throws InterruptedException {
        int actionType = crawlerActionInfo.getActionType();
        String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();
        String urlAddr;

        switch (actionType){
            case 2:
                String crawlerRegex = crawlerActionInfo.getCrawlerRegex();
                urlAddr = crawlerActionInfo.getUrlAddr();
                int index= CrawlerUtil.extractIntOfStr(urlAddr,"~","}");
                index=1;
                urlAddr=urlAddr.substring(0,urlAddr.indexOf( "{" ));
                detailUrlsPageProcessor.setRegex(crawlerRegex);
                for (int i = 1; i <= index; i++) {
                   detailUrlsPageProcessor.process(crawlerRegex,baseUrlAddr+urlAddr+i+".html");
                }
                break;
            case 3:
                http://www.cbrc.gov.cn/chinese/newShouDoc/DCD3ED9C2B2A49ABB0EBC90F311CA3C0.html
                urlAddr = crawlerActionInfo.getUrlAddr();
                int urlType = crawlerActionInfo.getUrlType();
                int actionId=CrawlerUtil.extractIntOfStr( urlAddr,"[","]" );
                crawerActionDynamicInfoDao.deleteAll(actionId);
                CrawerActionDynamicInfo crawerActionDynamicInfo;
                CrawlerNewsMilestone crawlerNewsMilestone;
                List<String> urlAddrs = detailUrlsPageProcessor.getList();
                for (String addr : urlAddrs) {
                    crawerActionDynamicInfo=new CrawerActionDynamicInfo();
                    crawerActionDynamicInfo.setActionId( actionId );
                    crawerActionDynamicInfo.setActionType(actionType);
                    crawerActionDynamicInfo.setUrlType(urlType);
                    crawerActionDynamicInfo.setUrlAddr( addr );
                    crawerActionDynamicInfoDao.insertAll(crawerActionDynamicInfo);
                    crawlerNewsMilestone=new  CrawlerNewsMilestone();
                    crawlerNewsMilestone.setActionId( actionId);
                    crawlerNewsMilestone.setUrlAddr( addr );
                    int existsUrl = crawlerNewsMilestoneDao.isExistsUrl( crawlerNewsMilestone );
                    if (existsUrl==0) {
                        crawlerNewsMilestoneDao.insertAll( crawlerNewsMilestone );
                    }
                }
                urlAddrs=crawlerNewsMilestoneDao.findUrlAddrsByNewDate( actionId );
                for (String addr : urlAddrs) {
                    cbrcDetailPageProcessor.process( baseUrlAddr+addr);
                }
                break;
        }
    }

    @Override
    public CrawlerActionInfo findOneByActionById(Integer actionId) {
        return  crawlerActionInfoDao.findOneByActionById(actionId);
    }
}
