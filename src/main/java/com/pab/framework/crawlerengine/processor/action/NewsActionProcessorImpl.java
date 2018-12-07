package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.dao.CrawerActionDynamicInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerNewsMilestoneDao;
import com.pab.framework.crawlerdb.domain.CrawerActionDynamicInfo;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerArticle;
import com.pab.framework.crawlerdb.domain.CrawlerNewsMilestone;
import com.pab.framework.crawlerengine.processor.DetailPageProcessor;
import com.pab.framework.crawlerengine.processor.DetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.processor.LoginDetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.util.CrawlerUtil;
import com.pab.framework.crawlerengine.util.FileUtils;
import com.pab.framework.crawlerengine.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
public class NewsActionProcessorImpl implements ActionProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DetailUrlsPageProcessor detailUrlsPageProcessor;
    @Autowired
    private DetailPageProcessor detailPageProcessor;
    @Autowired
    private LoginDetailUrlsPageProcessor loginDetailUrlsPageProcessor;
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;
    @Autowired
    private CrawerActionDynamicInfoDao crawerActionDynamicInfoDao;
    @Autowired
    private CrawlerNewsMilestoneDao crawlerNewsMilestoneDao;
    private List<String> urlAddrs = null;

    @Override
    public List<String> getUrlAddrs(CrawlerActionInfo crawlerActionInfo) {
        String crawlerRegex = crawlerActionInfo.getCrawlerRegex();
        String urlAddr = crawlerActionInfo.getUrlAddr();
        String prefix = urlAddr.substring(0, urlAddr.indexOf("{"));
        String suffix = urlAddr.substring(urlAddr.indexOf("}") + 1);
        int index = UrlUtils.maxPage(urlAddr);
        String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();
        for (int i = 1; i <= index; i++) {
            if ("https://xueqiu.com".equals(baseUrlAddr)) {
                prefix = "/v4/statuses/user_timeline.json?page=";
                suffix = "&user_id=7558914709";
                loginDetailUrlsPageProcessor.process(baseUrlAddr + prefix + i + suffix);
                return loginDetailUrlsPageProcessor.getList();
            } else {

                detailUrlsPageProcessor.setRegex(crawlerRegex);
                detailUrlsPageProcessor.process(crawlerRegex, baseUrlAddr + prefix + i + suffix);
                return detailUrlsPageProcessor.getList();
            }

        }
        return null;
    }

    @Override
    public void process(Integer actionId) {
        CrawlerActionInfo crawlerActionInfo = crawlerActionInfoDao.findOneByActionById(actionId);
        int actionType = crawlerActionInfo.getActionType();
        switch (actionType) {
            case 2:
                urlAddrs = getUrlAddrs(crawlerActionInfo);
                break;
            case 3:
                String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();
                Integer urlType = crawlerActionInfo.getUrlType();
                String urlAddr = crawlerActionInfo.getUrlAddr();
                actionId = CrawlerUtil.extractIntOfStr(urlAddr, "[", "]");
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
                try {
                    FileUtils.write(actionDesc, crawlerArticles);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }


}