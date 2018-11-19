package com.pab.framework.crawlerengine.crawler.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.3.2
 */
public class PageUrlsPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private List<String> list=new ArrayList<String>();

    public List<String> getList() {
        return list;
    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html = page.getHtml();
        String regex = "http://www.cbrc.gov.cn/chinese/newListDoc/\\d+/\\d+.html";
        page.addTargetRequests(html.links().regex(regex).all());
        if (page.getStatusCode()==200){
            list.add(page.getUrl().get());
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

}
