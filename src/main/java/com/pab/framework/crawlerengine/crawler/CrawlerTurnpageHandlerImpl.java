package com.pab.framework.crawlerengine.crawler;

import com.pab.framework.crawlercore.constant.Global;
import com.pab.framework.crawlerengine.enums.UrlTypeEnum;
import com.pab.framework.crawlerengine.service.ProxyService;
import com.pab.framework.crawlerengine.model.CrawlerJobInfo;
import com.pab.framework.crawlerengine.model.DynamicInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CrawlerTurnpageHandlerImpl implements CrawlerHandler {

    private InheritableThreadLocal<Integer> urlType = new InheritableThreadLocal();
    private InheritableThreadLocal<String[]> regex = new InheritableThreadLocal();
    private InheritableThreadLocal<List<Cookie>> cookies = new InheritableThreadLocal();

    @Autowired
    ProxyService proxyService;

    public List<DynamicInfo> handler(CrawlerJobInfo crawlerJobInfo)
            throws Exception {
        this.regex.set(crawlerJobInfo.getRegex().split(Global.CRAWLER_REGEX_SPLIT1));
        this.urlType.set(crawlerJobInfo.getUrlType());
        Spider spider = Spider.create(this).thread(5);
        List<DynamicInfo> dynamicUrls = new ArrayList();
        for (Object url : crawlerJobInfo.getGetUrls()) {
            ResultItems resultItems = spider.get((String) url);
            if ((resultItems == null) || (resultItems.get(DynamicInfo.class.getName()) == null)) {
                log.error("没有可爬取的内容,url:" + url + " regex:" + crawlerJobInfo.getRegex());
            } else {
                dynamicUrls.addAll(resultItems.get(DynamicInfo.class.getName()));
            }
            TimeUnit.SECONDS.sleep(1L);
        }
        return dynamicUrls;
    }

    public void process(Page page) {
        try {
            List<DynamicInfo> dynamicInfos = new ArrayList();
            if (this.urlType.get().intValue() == UrlTypeEnum.HTML.getLabel()) {
                List<String> ids = page.getHtml().xpath(this.regex.get()[0]).all();
                List<String> articles = page.getHtml().xpath(this.regex.get()[1]).all();
                List<String> contents = page.getHtml().xpath(this.regex.get()[2]).links().all();
                for (int i = 0; i < (contents.size() > articles.size() ? contents.size() : articles.size()); i++) {
                    DynamicInfo dynamicInfo = new DynamicInfo();
                    dynamicInfo.setId(ids.get(i));
                    dynamicInfo.setArticle(articles.get(i));
                    dynamicInfo.setContent(contents.get(i));
                    dynamicInfos.add(dynamicInfo);
                }
            } else if (this.urlType.get().intValue() == UrlTypeEnum.JSON.getLabel()) {
            }
            page.putField(DynamicInfo.class.getName(), dynamicInfos);
        } catch (Exception e) {
            log.error("处理爬取内容失败:" + page.getUrl(), e);
        }
    }


    public Site getSite() {
        Site site = Site.me().setRetryTimes(10).setSleepTime(1000).setRetrySleepTime(1000).setTimeOut(5000).setUseGzip(true).addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

        if (this.cookies.get() != null) {
            for (Cookie cookie : this.cookies.get()) {
                site.addCookie(cookie.getName(), cookie.getValue());
            }
        }
        return site;
    }

    public static void main(String[] args) throws Exception {
        CrawlerTurnpageHandlerImpl chi = new CrawlerTurnpageHandlerImpl();
        CrawlerJobInfo cji = new CrawlerJobInfo();
        List<String> urls = new ArrayList();
        urls.add("https://www.wdzj.com");
        cji.setGetUrls(urls);
        cji.setUrlType(Integer.valueOf(1));
        cji.setRegex("//div[@class='tabclist on']/ul[@class='newslist']//a[@title]");
        chi.handler(cji);
    }
}