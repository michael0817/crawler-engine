package com.pab.framework.crawlerengine.crawler.processor;

import org.apache.commons.lang.StringEscapeUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.3.2
 */
public class KaxunDetailPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private List<String> list = new ArrayList<String>();

    public List<String> getList() {
        return list;
    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html = page.getHtml();
        String tit = html.xpath("h1[@class='nr01_tit']//text()").get();
        list.add(tit);
        String right01_date = html.xpath("p[@class='right01_date']/text(2)").get();
        list.add(right01_date);
        String right01_nr=html.xpath("div[@class='right01_nr']").get();
        List<String> strings=html.xpath("div[@class='right01_nr']/text()").all();
        int size = strings.size();
        String string;
        for (int i = 0; i < size; i++) {
            string=strings.get(i);
            string= StringEscapeUtils.unescapeHtml(string);
            list.add(string);
        }
  }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        KaxunDetailPageProcessor process = new KaxunDetailPageProcessor();
        //http://www.51kaxun.com/news/13433.html
        Spider spider = Spider.create(process).addUrl("http://www.51kaxun.com/news/13433.html");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped) == 0) {
            System.out.println(process.getList());
        }

    }
}
