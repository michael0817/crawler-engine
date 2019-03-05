package com.pab.framework.crawlerengine.crawler;

import com.pab.framework.crawlercore.constant.Global;
import com.pab.framework.crawlerengine.enums.UrlTypeEnum;
import com.pab.framework.crawlerengine.service.ProxyService;
import com.pab.framework.crawlerengine.model.CrawlerJobInfo;
import com.pab.framework.crawlerengine.model.News;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class CrawlerNewsHandlerImpl implements CrawlerHandler {

    private InheritableThreadLocal<Integer> urlType = new InheritableThreadLocal();
    private InheritableThreadLocal<String[]> regex = new InheritableThreadLocal();
    private InheritableThreadLocal<List<Cookie>> cookies = new InheritableThreadLocal();

    @Autowired
    ProxyService proxyService;

    public List<News> handler(CrawlerJobInfo crawlerJobInfo)
            throws Exception {
        List<News> newsList = new ArrayList();
		this.regex.set(crawlerJobInfo.getRegex().split(Global.CRAWLER_REGEX_SPLIT1));
		if (this.regex.get().length != 4) { throw new Exception("xpath数量不正确:" +
		this.regex.get().length); } this.urlType.set(crawlerJobInfo.getUrlType());
        Spider spider = Spider.create(this).thread(4);
        List<ResultItems> resultItemsList = spider.getAll(crawlerJobInfo.getGetUrls());
        if ((resultItemsList == null) || (resultItemsList.size() == 0)) {
            log.error("没有可爬取的内容,regex:" + crawlerJobInfo.getRegex());
        } else {
            for (ResultItems ri : resultItemsList) {
                newsList.add(ri.get(News.class.getName()));
            }
            TimeUnit.SECONDS.sleep(1L);
        }
        return newsList;
    }

    public void process(Page page) {
        try {
            News news = new News();
            if (this.urlType.get().intValue() == UrlTypeEnum.HTML.getLabel()) {
                if (StringUtils.isNotBlank(this.regex.get()[0])) {
                    news.setTitle(page.getHtml().xpath(this.regex.get()[0]).get());
                    if (StringUtils.isBlank(news.getTitle())) {
                        news.setTitle("无标题");
                    }
                }
                if (StringUtils.isNotBlank(this.regex.get()[1])) {
                    news.setDate(page.getHtml().xpath(this.regex.get()[1]).get());
                }
                if (StringUtils.isNotBlank(this.regex.get()[2])) {
                    news.setBrief(page.getHtml().xpath(this.regex.get()[2]).get());
                }
                if (StringUtils.isNotBlank(this.regex.get()[3])) {
                    news.setContent(page.getHtml().xpath(this.regex.get()[3]).all());
                    if (news.getContent().size() == 0) {
                        news.getContent().add("该条新闻格式异常无法爬取，请直接访问下面的链接：");
                        news.getContent().add(page.getUrl().toString());
                    }
                }
            }

            page.putField(News.class.getName(), news);
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
        CrawlerNewsHandlerImpl chi = new CrawlerNewsHandlerImpl();
        CrawlerJobInfo cji = new CrawlerJobInfo();
        List<String> urls = new ArrayList();

        urls.add("https://www.wdzj.com/news/yc/369377299.html");
        cji.setGetUrls(urls);
        cji.setUrlType(Integer.valueOf(UrlTypeEnum.HTML.getLabel()));
        cji.setRegex("/html/body//h1[@class='page-title']/text()||/html/body//div[@class='page-time']/span[2]/text()||/html/body//div[@class='page-summary']/div/text()||/html/body//div[@class='page-content']/p/allText()");
        chi.handler(cji);
    }

}