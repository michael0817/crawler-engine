package com.pab.framework.crawlerengine.crawler.processor;

import com.pab.framework.crawlerdb.domain.CrawlerArticle;
import com.pab.framework.crawlerengine.crawler.factory.Detail;
import com.pab.framework.crawlerengine.crawler.factory.DetailFactory;
import com.pab.framework.crawlerengine.crawler.util.CrawlerUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

@Component
public class DetailPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private String title;
    private String date;
    private String content;
    private String domain;
    private Spider spider;
    private List<CrawlerArticle> crawlerArticles = new ArrayList<CrawlerArticle>();

    public String getTitle() {
        return spider.getStatus().compareTo(Spider.Status.Stopped) == 0 ? title : null;
    }

    public String getDate() {
        return spider.getStatus().compareTo(Spider.Status.Stopped) == 0 ? date : null;
    }

    public String getContent() {
        return spider.getStatus().compareTo(Spider.Status.Stopped) == 0 ? content : null;
    }

    @Override
    public void process(Page page) {
        Detail detail = DetailFactory.getDetail(domain);
        title = detail.getTitle(page);
        date = detail.getDate(page);
        content = detail.getContent(page);
        CrawlerArticle crawlerArticle = new CrawlerArticle();
        crawlerArticle.setTitle(title);
        crawlerArticle.setDate(date);
        crawlerArticle.setContent(content);
        if (title != null && content != null) {
            crawlerArticles.add(crawlerArticle);
        }

    }

    @Override
    public Site getSite() {
        return site;
    }



    public List<CrawlerArticle> process(String baseUrlAddr, List<String> urlAddrs) {
        int size = urlAddrs.size();
        String[] url_addrs = new String[urlAddrs.size()];

        for (int i = 0; i < size; i++) {
            if (urlAddrs.get(i).startsWith(baseUrlAddr)) {
                url_addrs[i] = urlAddrs.get(i);
            } else {
                url_addrs[i] = baseUrlAddr + urlAddrs.get(i);
            }
            domain = CrawlerUtil.domainStr(url_addrs[i]);
        }
        spider = Spider.create(this).addUrl(url_addrs).thread(5);
        spider.run();
        if (Spider.Status.Stopped.compareTo(spider.getStatus()) == 0) {
            return crawlerArticles;
        }
        return null;
    }


    public static void main(String[] args) {
        DetailPageProcessor detailPageProcessor = new DetailPageProcessor();
        List<String> urlAddrs = new ArrayList<>();
        urlAddrs.add("http://www.cbrc.gov.cn/chinese/newShouDoc/2F17C2C95E724066818561C7D2F622D8.html");
       detailPageProcessor.process("http://www.cbrc.gov.cn", urlAddrs);
    }
}
