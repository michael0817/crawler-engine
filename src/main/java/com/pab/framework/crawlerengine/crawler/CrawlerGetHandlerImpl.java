package com.pab.framework.crawlerengine.crawler;

import com.pab.framework.crawlercore.constant.Global;
import com.pab.framework.crawlerengine.enums.UrlTypeEnum;
import com.pab.framework.crawlerengine.service.ProxyService;
import com.pab.framework.crawlerengine.vo.CrawlerJobInfo;
import lombok.extern.slf4j.Slf4j;
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
public class CrawlerGetHandlerImpl implements CrawlerHandler {

    private InheritableThreadLocal<Integer> urlType = new InheritableThreadLocal();
    private InheritableThreadLocal<String> regex = new InheritableThreadLocal();
    private InheritableThreadLocal<List<Cookie>> cookies = new InheritableThreadLocal();

    @Autowired
    ProxyService proxyService;

    public List<String> handler(CrawlerJobInfo crawlerJobInfo)
            throws Exception {
        List<String> htmlList = new ArrayList();
        this.regex.set(crawlerJobInfo.getRegex());
        this.urlType.set(crawlerJobInfo.getUrlType());
        Spider spider = Spider.create(this).thread(4);
        List<ResultItems> resultItemsList = spider.getAll(crawlerJobInfo.getGetUrls());
        if ((resultItemsList == null) || (resultItemsList.size() == 0)) {
            log.error("没有可爬取的内容,regex:" + crawlerJobInfo.getRegex());
        } else {
            for (ResultItems ri : resultItemsList) {
                htmlList.addAll(ri.get(Global.CRAWLER_HTML));
            }
            TimeUnit.SECONDS.sleep(1L);
        }
        return htmlList;
    }

    public void process(Page page) {
        try {
            page.putField(Global.CRAWLER_HTML, page.getHtml().xpath(this.regex.get()).all());
        } catch (Exception e) {
            log.error("处理爬取内容失败:" + page.getUrl(), e);
        }
    }


    public Site getSite() {
        Site site = Site.me()
                .setRetryTimes(10)
                .setSleepTime(1000)
                .setRetrySleepTime(1000)
                .setTimeOut(5000)
                .setUseGzip(true)
                .setUserAgent("Mozilla/5.0 (Linux; Android 6.0.1; DUK-AL20 Build/MXC89K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/44.0.2403.119 Mobile Safari/537.36");

        if (this.cookies.get() != null) {
            for (Cookie cookie : this.cookies.get()) {
                site.addCookie(cookie.getName(), cookie.getValue());
            }
        }
        return site;
    }

    public static void main(String[] args) throws Exception {
        CrawlerGetHandlerImpl chi = new CrawlerGetHandlerImpl();
        CrawlerJobInfo cji = new CrawlerJobInfo();
        List<String> urls = new ArrayList();
        //urls.add("https://ssl.mall.cmbchina.com/_CL5_/Category/GetAllCategories");
        urls.add("https://ssl.mall.cmbchina.com/_CL5_/Category/GetCategories?id=5");
        cji.setGetUrls(urls);
        cji.setUrlType(Integer.valueOf(UrlTypeEnum.HTML.getLabel()));
        //cji.setRegex("//div[@id='divC1Panel']//li");
        cji.setRegex("//dl[@class='catalog_list']//dd");
        List<String> htmlList = chi.handler(cji);
        System.out.println(htmlList.size());



    }

}