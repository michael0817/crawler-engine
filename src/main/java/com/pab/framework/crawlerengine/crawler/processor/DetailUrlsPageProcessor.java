package com.pab.framework.crawlerengine.crawler.processor;

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
        String regex = "http://www.cbrc.gov.cn/chinese/newShouDoc/\\w+.html";
        Selectable xpath = html.links().regex(regex);
        if (xpath.match()) {
            list.addAll(xpath.all());
        }
    }


    @Override
    public Site getSite() {
        return site;
    }

}
