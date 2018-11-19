package com.pab.framework.crawlerengine.crawler.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.6.0
 */
public class IresearchDetailPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private List<String> list = new ArrayList<String>();

    public List<String> getList() {
        return list;
    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html = page.getHtml();
        String tit = html.xpath("h1[@class='tit']/text()").get();
        list.add(tit);
        String report = html.xpath("div[@class='m-report-lead']/p/text()").get();
        list.add(report);

    }


    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        IresearchDetailPageProcessor pageProcessor = new IresearchDetailPageProcessor();
        Spider spider = Spider.create(pageProcessor).addUrl("http://www.iresearch.com.cn/Detail/report?id=3296&isfree=0");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped) == 0) {
            System.out.println(pageProcessor.list);
        }
    }

}
