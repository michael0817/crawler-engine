package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerengine.crawler.processor.DetailPageProcessor;
import com.pab.framework.crawlerengine.crawler.processor.DetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.crawler.processor.LoginDetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.crawler.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                return  loginDetailUrlsPageProcessor.getList();
            } else {

                detailUrlsPageProcessor.setRegex(crawlerRegex);
                detailUrlsPageProcessor.process(crawlerRegex, baseUrlAddr + prefix + i + suffix);
                return  detailUrlsPageProcessor.getList();
            }

        }
        return null;
    }


}