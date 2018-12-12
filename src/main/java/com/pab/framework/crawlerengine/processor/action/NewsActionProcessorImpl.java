package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.dao.CrawerActionDynamicInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerNewsMilestoneDao;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerengine.crawler.CrawlerHandler;
import com.pab.framework.crawlerengine.processor.DetailPageProcessor;
import com.pab.framework.crawlerengine.processor.DetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.processor.LoginDetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
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
    @Autowired
    private CrawlerHandler crawlerHandler;

    private List<String> urls = new ArrayList<>();

    @Override
    public List<String> getUrlAddrs(CrawlerActionInfo crawlerActionInfo) {

        List<String> urlAddrs = new LinkedList<>();
        String urlAddr = crawlerActionInfo.getUrlAddr();
        String prefix = urlAddr.substring(0, urlAddr.indexOf("{"));
        String suffix = urlAddr.substring(urlAddr.indexOf("}") + 1);
        int index = UrlUtils.maxPage(urlAddr);
        String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();
        String crawlerRegex = crawlerActionInfo.getCrawlerRegex();
        for (int i = 1; i <= index; i++) {
            if ("https://xueqiu.com".equals(baseUrlAddr)) {
                prefix = "/v4/statuses/user_timeline.json?page=";
                suffix = "&user_id=7558914709";
            }
            urlAddrs.add(baseUrlAddr + prefix + i + suffix);
        }

        if ("https://xueqiu.com".equals(baseUrlAddr)) {
            urls.addAll(loginDetailUrlsPageProcessor.process(urlAddrs));

        } else {
            urls.addAll(detailUrlsPageProcessor.process(crawlerRegex, urlAddrs));
        }
        return urls;

    }

    @Override
    public void process(Integer actionId) throws IOException {
        CrawlerActionInfo crawlerActionInfo = crawlerActionInfoDao.findCrawlerActionInfo(actionId);
        String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();

        int actionType = crawlerActionInfo.getActionType();
        switch (actionType) {
            case 2:
                urls = getUrlAddrs(crawlerActionInfo);
                break;
            case 3:
                crawlerHandler.handler(crawlerActionInfo, urls);
                break;

        }

    }
}