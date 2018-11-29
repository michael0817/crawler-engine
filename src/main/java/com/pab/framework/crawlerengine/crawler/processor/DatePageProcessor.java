package com.pab.framework.crawlerengine.crawler.processor;

import com.pab.framework.crawlerengine.crawler.factory.Detail;
import com.pab.framework.crawlerengine.crawler.factory.DetailFactory;
import com.pab.framework.crawlerengine.crawler.util.CrawlerUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.net.MalformedURLException;

@Component
public class DatePageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes( 3 ).setSleepTime( 1000 ).setTimeOut( 10000 );
    private String date;
    private String domain;
    private Spider spider;
    public String getDate() {
    return spider.getStatus().compareTo( Spider.Status.Stopped)==0?date:null;
    }

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Detail detail = DetailFactory.getDetail( domain );
        date = detail.getDate(page);
    }

    public void process(String urlStr) {
        spider = Spider.create( this ).addUrl( urlStr ).thread( 5 );
        domain= CrawlerUtil.domainStr(urlStr);
        spider.run();
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws MalformedURLException {
        String urlStr="http://www.cbrc.gov.cn/chinese/newShouDoc/DCD3ED9C2B2A49ABB0EBC90F311CA3C0.html";
        DatePageProcessor datePageProcessor=new DatePageProcessor();
        datePageProcessor.process( urlStr );
        System.out.println(datePageProcessor.getDate());
    }
}
