package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.dao.CrawerActionDynamicInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerNewsMilestoneDao;
import com.pab.framework.crawlerdb.domain.CrawerActionDynamicInfo;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerArticle;
import com.pab.framework.crawlerdb.domain.CrawlerNewsMilestone;
import com.pab.framework.crawlerengine.crawler.processor.DetailPageProcessor;
import com.pab.framework.crawlerengine.crawler.processor.DetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.crawler.processor.LoginDetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.crawler.util.CrawlerUtil;
import com.pab.framework.crawlerengine.crawler.util.FileUtils;
import com.pab.framework.crawlerengine.crawler.util.UrlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private CrawerActionDynamicInfoDao crawerActionDynamicInfoDao;
    @Autowired
    private CrawlerNewsMilestoneDao crawlerNewsMilestoneDao;
    @Autowired
    private DetailPageProcessor detailPageProcessor;
    List<String> urlAddrs =null;

    private StringBuilder builder = new StringBuilder();

    @Override
    public void processor(CrawlerActionInfo crawlerActionInfo) throws InterruptedException, IOException {
        int actionType = crawlerActionInfo.getActionType();
        String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();
        String urlAddr;
        CrawlerArticle article;
        LoginDetailUrlsPageProcessor loginDetailUrlsPageProcessor=null;

        switch (actionType) {
            case 2:
                String crawlerRegex = crawlerActionInfo.getCrawlerRegex();
                urlAddr = crawlerActionInfo.getUrlAddr();
                int index = 1;
                index = UrlUtils.maxPage(urlAddr);
                String prefix = urlAddr.substring(0, urlAddr.indexOf("{"));
                String suffix = urlAddr.substring(urlAddr.indexOf("}") + 1);
                Spider spider;
                for (int i = 1; i <= index; i++) {
                    if ("https://xueqiu.com".equals(baseUrlAddr)) {
                        //https://xueqiu.com/v4/statuses/user_timeline.json?page=1&user_id=7558914709
                        prefix = "/v4/statuses/user_timeline.json?page=";
                        suffix = "&user_id=7558914709";
                        loginDetailUrlsPageProcessor=new LoginDetailUrlsPageProcessor();
                         spider=   Spider.create(loginDetailUrlsPageProcessor)
                                .addUrl("https://xueqiu.com/v4/statuses/user_timeline.json?page="+i+"&user_id=7558914709");
                        if (spider.getStatus().compareTo(Spider.Status.Init)==0){
                            spider.run();
                        }

                        if (spider.getStatus().compareTo(Spider.Status.Stopped)==0){
                            urlAddrs=loginDetailUrlsPageProcessor.getList();
                            System.out.println(urlAddrs);
                        }
                    } else {
                        detailUrlsPageProcessor.setRegex(crawlerRegex);
                        detailUrlsPageProcessor.process(crawlerRegex, baseUrlAddr + prefix + i + suffix);
                    }

                }
                break;
            case 3:
                urlAddr = crawlerActionInfo.getUrlAddr();
                int urlType = crawlerActionInfo.getUrlType();
                int actionId = CrawlerUtil.extractIntOfStr(urlAddr, "[", "]");
                String actionDesc = crawlerActionInfo.getActionDesc();
                actionDesc += DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss");
                actionDesc += ".txt";
                String dir = System.getProperty("user.dir");
                dir += File.separator + "out" + File.separator + actionDesc;
                crawerActionDynamicInfoDao.deleteAll(actionId);
                CrawerActionDynamicInfo crawerActionDynamicInfo;
                CrawlerNewsMilestone crawlerNewsMilestone;

                if (!"https://xueqiu.com".equals(baseUrlAddr)){
                    urlAddrs= detailUrlsPageProcessor.getList();
                }

                CrawlerArticle crawlerArticle;
                List<CrawlerArticle> crawlerArticles = null;
                int size = 1;
                if (CollectionUtils.isNotEmpty(urlAddrs)) {
                    crawlerArticles = new ArrayList<>();
                    size = urlAddrs.size();
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

                if (CollectionUtils.isNotEmpty(urlAddrs)) {
                    size = urlAddrs.size();
                    for (int i = 0; i < size; i++) {
                        crawlerArticle = new CrawlerArticle();
                        if (urlAddrs.get(i).startsWith(baseUrlAddr)) {

                            detailPageProcessor.process(urlAddrs.get(i));
                        } else {

                            detailPageProcessor.process(baseUrlAddr + urlAddrs.get(i));
                        }
                        builder.append("第" + (i + 1) + "篇文章开始\n");
                        title = detailPageProcessor.getTitle();
                        if (title != null) {
                            title = title.trim();
                            if (!title.isEmpty()) {
                                crawlerArticle.setTitle(title);
                                builder.append(title);
                                builder.append("\n");
                            }

                        }
                        date = detailPageProcessor.getDate();
                        if (date != null) {
                            date = date.trim();
                            if (!date.isEmpty()) {
                                crawlerArticle.setDate(date);
                                builder.append(date);
                                builder.append("\n");
                            }

                        }
                        content = detailPageProcessor.getContent();
                        if (content != null) {
                            content = content.trim();
                            if (content != null) {
                                crawlerArticle.setContent(content);
                                builder.append(content);
                                builder.append("\n");
                            }

                        }
                        builder.append("第" + (i + 1) + "篇文章结束\n\n");
                        if (title != null && content != null) {
                            if (CollectionUtils.isNotEmpty(crawlerArticles)){

                                crawlerArticles.add(crawlerArticle);
                            }

                        }
                    }
                }

                if (crawlerArticles != null) {
                    FileUtils.write(dir, builder.toString());
                }
                break;
        }
    }

    @Override
    public CrawlerActionInfo findOneByActionById(Integer actionId) {
        return crawlerActionInfoDao.findOneByActionById(actionId);
    }
}
