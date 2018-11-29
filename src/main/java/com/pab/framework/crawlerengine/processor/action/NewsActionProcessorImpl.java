package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.dao.CrawerActionDynamicInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerNewsMilestoneDao;
import com.pab.framework.crawlerdb.domain.CrawerActionDynamicInfo;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerArticle;
import com.pab.framework.crawlerdb.domain.CrawlerNewsMilestone;
import com.pab.framework.crawlerengine.crawler.processor.ContentPageProcessor;
import com.pab.framework.crawlerengine.crawler.processor.DatePageProcessor;
import com.pab.framework.crawlerengine.crawler.processor.DetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.crawler.processor.TitlePageProcessor;
import com.pab.framework.crawlerengine.crawler.util.CrawlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
public class NewsActionProcessorImpl implements ActionProcessor {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;
    @Autowired
    private DetailUrlsPageProcessor detailUrlsPageProcessor;
    @Autowired
    private TitlePageProcessor titlePageProcessor;
    @Autowired
    private DatePageProcessor datePageProcessor;
    @Autowired
    private ContentPageProcessor contentPageProcessor;
    @Autowired
    private CrawerActionDynamicInfoDao crawerActionDynamicInfoDao;
    @Autowired
    private CrawlerNewsMilestoneDao crawlerNewsMilestoneDao;

    @Override
    public void processor(CrawlerActionInfo crawlerActionInfo) throws InterruptedException, MalformedURLException {
        int actionType = crawlerActionInfo.getActionType();
        String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();
        String urlAddr;
        switch (actionType) {
            case 2:
                String crawlerRegex = crawlerActionInfo.getCrawlerRegex();
                urlAddr = crawlerActionInfo.getUrlAddr();
                int index = 1;
                Pattern pattern= Pattern.compile("\\d+}");
                Matcher matcher = pattern.matcher(urlAddr);
                if (matcher.find()){
                    String group = matcher.group();
                    index= Integer.parseInt(group.substring(0, group.length()-1));
                }
                String prefix = urlAddr.substring(0, urlAddr.indexOf("{"));
                String suffix = urlAddr.substring(urlAddr.indexOf("}") + 1);
                detailUrlsPageProcessor.setRegex(crawlerRegex);
                for (int i = 1; i <= index; i++) {
                    detailUrlsPageProcessor.process(crawlerRegex, baseUrlAddr + prefix + i + suffix);
                }
                break;
            case 3:
                urlAddr = crawlerActionInfo.getUrlAddr();
                int urlType = crawlerActionInfo.getUrlType();
                int actionId = CrawlerUtil.extractIntOfStr(urlAddr, "[", "]");
                crawerActionDynamicInfoDao.deleteAll(actionId);
                CrawerActionDynamicInfo crawerActionDynamicInfo;
                CrawlerNewsMilestone crawlerNewsMilestone;
                List<String> urlAddrs = detailUrlsPageProcessor.getList();
                CrawlerArticle crawlerArticle;
                List<CrawlerArticle> crawlerArticles = new ArrayList<>();
                int size=1;
                if (urlAddrs != null) {
                    size=urlAddrs.size();
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
                }
                urlAddrs = crawlerNewsMilestoneDao.findUrlAddrsByNewDate(actionId);
                String title = null;
                String date = null;
                String content = null;
                if (urlAddrs != null) {
                    size=urlAddrs.size();
                    for (int i = 0; i < size; i++) {
                        crawlerArticle = new CrawlerArticle();
                        if (urlAddrs.get(i).startsWith(baseUrlAddr)){
                            titlePageProcessor.process(urlAddrs.get(i));
                            datePageProcessor.process(urlAddrs.get(i));
                            contentPageProcessor.process(urlAddrs.get(i));
                        }
                        else{
                            titlePageProcessor.process(baseUrlAddr + urlAddrs.get(i));
                            datePageProcessor.process(baseUrlAddr + urlAddrs.get(i));
                            contentPageProcessor.process(baseUrlAddr +urlAddrs.get(i));
                        }
                        title=titlePageProcessor.getTitle();
                        if (title != null) {
                            crawlerArticle.setTitle(title);
                        }
                        date=datePageProcessor.getDate();
                        if (date != null) {
                            crawlerArticle.setDate(date);
                        }
                        content = contentPageProcessor.getContent();
                        if (content != null) {
                            crawlerArticle.setContent(content);
                        }
                        if (title!=null&&content!=null){
                            crawlerArticles.add(crawlerArticle);
                        }
                    }
                    for (CrawlerArticle article : crawlerArticles) {
                        logger.info("title={}",article.getTitle());
                        logger.info("date={}",article.getDate());
                        logger.info("content={}",article.getContent());
                    }
                }
                break;
        }
    }

    @Override
    public CrawlerActionInfo findOneByActionById(Integer actionId) {
        return crawlerActionInfoDao.findOneByActionById(actionId);
    }
}
