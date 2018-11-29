package com.pab.framework.crawlerengine.crawler.processor;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
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
public class DetailUrlsAnalyticalPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private List<String> list = new ArrayList<String>();

    public List<String> getList() {
        return list;
    }

    @Override
    public void process(Page page) {

        page.setCharset("utf-8");
        Html html = page.getHtml();
        Selectable xpath = html.xpath("*[@id='testUI']/tbody/tr");
        int size = xpath.nodes().size();
        String title;
        String date;
        for (int i = 0; i < size; i++) {
            title = html.xpath("*[@id='testUI']/tbody/tr[" + i + "]/td/a[@title]/text()").get();
            date = html.xpath("*[@id='testUI']/tbody/tr[" + i + "]/td[2]/text()").get();
            if (title != null && date != null) {
                list.add(title + " " + date);
            }
        }
    }


    @Override
    public Site getSite() {
        return site;
    }

}
