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
public class XueQiuDetailPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private List<String> list = new ArrayList<String>();

    public List<String> getList() {
        return list;
    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html = page.getHtml();
        String source = html.xpath("span[@class='source']/text()").get();
        list.add(source);
        String time = html.xpath("a[@class='time']/text()").get();
        list.add(time);
        String article__bd__title=html.xpath("h1[@class='article__bd__title']/text()").get();
        list.add(article__bd__title);
        //article__bd__detail
        List<String> strings=html.xpath("div[@class='article__bd__detail']/p/text()").all();
        for (String string : strings) {
            string=string.trim();
            if(!string.isEmpty()){
                list.add(string);
            }

        }
    }


    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        XueQiuDetailPageProcessor pageProcessor = new XueQiuDetailPageProcessor();
        Spider spider = Spider.create(pageProcessor).addUrl("https://xueqiu.com/7558914709/116919695");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped) == 0) {
            System.out.println(pageProcessor.list);
        }
    }

}
