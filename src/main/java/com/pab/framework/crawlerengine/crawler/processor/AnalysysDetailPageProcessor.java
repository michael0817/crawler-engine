package com.pab.framework.crawlerengine.crawler.processor;

import org.springframework.stereotype.Component;
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
@Component
public class AnalysysDetailPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private List<String> list = new ArrayList<String>();

    public List<String> getList() {
        return list;
    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html = page.getHtml();
        String summary = html.xpath("div[@class='summary']/text()").get();
        list.add(summary);
    }


    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        AnalysysDetailPageProcessor pageProcessor = new AnalysysDetailPageProcessor();
        Spider spider = Spider.create(pageProcessor).addUrl("https://www.analysys.cn/article/analysis/detail/20018996");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped) == 0) {
            System.out.println(pageProcessor.list);
        }
    }

}
