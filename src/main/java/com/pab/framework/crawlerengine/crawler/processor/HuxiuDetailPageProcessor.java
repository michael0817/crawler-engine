package com.pab.framework.crawlerengine.crawler.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.6.0
 */
public class HuxiuDetailPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private List<String> list=new ArrayList<String>();

    public List<String> getList() {
        return list;
    }
    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html = page.getHtml();
        String t_h1=html.xpath("h1[@class='t-h1']/text()").get();
        list.add(t_h1);
        String author_name=html.xpath("span[@class='author-name']/a/text()").get();
        list.add(author_name);
        Selectable xpath = html.xpath("div[@class='column-link-box']/span/text()");
        List<String> strings = xpath.all();
        String link = html.xpath("a[@class='column-link']/text()").get();
        list.add(strings.get(0)+" "+strings.get(1)+" "+strings.get(2)+" "+link);
        xpath = html.xpath("div[@class='article-content-wrap']/p/text()");
        strings= xpath.all();
        for (String string : strings) {
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

    public static void main(String[] args) throws IOException {
        HuxiuDetailPageProcessor processor=new HuxiuDetailPageProcessor();
        Spider spider= Spider.create(processor).addUrl("https://www.huxiu.com/article/261087.html?f=member_article");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped)==0){
            processor.list.forEach(str->{
                System.out.println(str);
            });
        }

    }

}
