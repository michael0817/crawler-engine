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
public class DetailUrlsPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private List<String> list = new ArrayList<String>();

    public List<String> getList() {
        return list;
    }


    @Override
    public void process(Page page) {

        page.setCharset("utf-8");
        Html html = page.getHtml();
        List<String> all = html.xpath("a[@title]").all();
        for (int i = 0; i < all.size(); i++) {
            list.add(all.get(i));
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        DetailUrlsPageProcessor process=new DetailUrlsPageProcessor();
        Spider spider=Spider.create(process).addUrl("http://www.cbrc.gov.cn/chinese/newListDoc/111003/1.html");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped)==0){
            System.out.println(process.getList());
        }
    }

}
