package com.pab.framework.crawlerengine.crawler.processor;

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
public class WdzjDetailPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private List<String> list = new ArrayList<String>();

    public List<String> getList() {
        return list;
    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html = page.getHtml();
        String title=html.xpath("h1[@class='page-title']/text()").get();
        list.add(title);
        String source=html.xpath("div[@class='page-time']/span[1]/text(1)").get();
        String sourceValue=html.xpath("div[@class='page-time']/span/em/text()").get();
        String _=html.xpath("div[@class='page-time']/text(2)").get();
        String auhtor=html.xpath("div[@class='page-time']/span[2]/text(1)").get();
        String auhtorValue=html.xpath("div[@class='page-time']/span[2]/em/text(1)").get();
        String time=html.xpath("div[@class='page-time']/span[3]/text()").get();
        list.add(source+sourceValue+" "+_+auhtor+auhtorValue+" "+_+time);
        String t = html.xpath("div[@class='page-summary']/span[@class='t']/text()").get();
        list.add(t);
        String  summary= html.xpath("div[@class='page-summary']/div/text()").get();
        list.add(summary);
        List<Selectable> nodes = html.xpath("div[@class='page-content']").nodes();
        String string;
        for (Selectable node : nodes) {
            string=node.replace("<[^<>]+>","").get();
            string=string.trim();
            if (!string.isEmpty()){
                list.add(string);
            }
        }
    }


    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        WdzjDetailPageProcessor pageProcessor = new WdzjDetailPageProcessor();
        Spider spider = Spider.create(pageProcessor).addUrl("https://www.wdzj.com/news/hydongtai/3432731.html");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped) == 0) {
            System.out.println(pageProcessor.list);
        }
    }

}
