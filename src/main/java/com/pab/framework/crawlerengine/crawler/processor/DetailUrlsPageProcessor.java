package com.pab.framework.crawlerengine.crawler.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private String regex;

    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html = page.getHtml();
        List<String> all = html.links().regex(regex).all();
        Set<String> set = new HashSet<String>();
        set.addAll(all);
        all.clear();
        all.addAll(set);
        int size = all.size();
        for (int i = 0; i < size; i++) {
            list.add(all.get(i));
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
    /**
     * /chinese/newShouDoc/\w+.html
     * /nifa/\d+/\d+/\d+/index.html
     * /nifa/\d+/\d+/\d+/index.html|http://www.pbc.gov.cn/goutongjiaoliu/\d+/\d+/\d+/index.html
     * http://www.51kaxun.com/news/13\d{3}.html
     * //www.wdzj.com/news/yc/343\d{2}1\d{1}.html
     * /Detail/report?id=\d+&amp;isfree=\d+
     *
     *  http://www.bugutime.com/news/\d+.html
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        DetailUrlsPageProcessor process = new DetailUrlsPageProcessor();
        process.setRegex("\\d+");
        Spider spider = Spider.create(process).addUrl("https://xueqiu.com/u/7558914709");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped) == 0) {
            // System.out.println(process.getList());
        }
    }

}
