package com.pab.framework.crawlerengine.crawler.processor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
        Document document = html.getDocument();
        Elements elements = document.getElementsByAttributeValue("class","page-time");
        for (Element element : elements) {
            list.add(element.text());
        }
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
