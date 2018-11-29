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
public class TitlePageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes( 3 ).setSleepTime( 1000 ).setTimeOut( 10000 );
    private String title;
    private String domain;
    private Spider spider;
    public String getTitle() {
        return spider.getStatus().compareTo( Spider.Status.Stopped)==0?title:null;
    }

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Detail detail = DetailFactory.getDetail( domain );
        title = detail.getTitle( page );
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void process(String urlStr) throws MalformedURLException {
        spider = Spider.create( this ).addUrl(urlStr).thread( 5 );
        domain= CrawlerUtil.domainStr(urlStr);
        spider.run();

    }
    public static void main(String[] args) throws MalformedURLException {
        TitlePageProcessor titlePageProcessor = new TitlePageProcessor();
        String urlStr="http://www.cbrc.gov.cn/chinese/newShouDoc/DCD3ED9C2B2A49ABB0EBC90F311CA3C0.html";
        titlePageProcessor.process( urlStr );
        System.out.println(titlePageProcessor.title);


    }
}
