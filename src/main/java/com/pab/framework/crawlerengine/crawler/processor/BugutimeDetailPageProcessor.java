package com.pab.framework.crawlerengine.crawler.processor;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.6.0
 */
@Component
public class BugutimeDetailPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private List<String> list = new ArrayList<String>();

    public List<String> getList() {
        return list;
    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html = page.getHtml();
        String title = html.xpath("h1[@class='view-title']/text()").get();
        list.add(title);
        String time = html.xpath("time/text()").get();
        list.add(time);
        List<Selectable> nodes = html.xpath("div[@class='view-content']/p").nodes();
        html.xpath("div[@class='view-content']/p").nodes();
        for (Selectable node : nodes) {
            list.add(node.replace("<[^<>]+>", "").get());
        }
    }


    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        BugutimeDetailPageProcessor pageProcessor = new BugutimeDetailPageProcessor();
        Spider spider = Spider.create(pageProcessor).addUrl("http://www.bugutime.com/news/8531.html");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped) == 0) {
            System.out.println(pageProcessor.list);
        }
    }

}
