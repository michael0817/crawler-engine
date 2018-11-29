package com.pab.framework.crawlerengine.crawler.processor;

import org.springframework.stereotype.Component;
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
@Component
public class IresearchDetailPageProcessor  implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private List<String> list=new ArrayList<String>();

    public List<String> getList() {
        return list;
    }
    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html = page.getHtml();
        String tit=html.xpath("h1[@class='tit']/text()").get();
        list.add(tit);
        Selectable xpath = html.xpath("div[@class='info']/span/text()");
        List<String> strings = xpath.all();
        list.add(strings.get(0)+strings.get(1)+" "+strings.get(2));
        String report=html.xpath("div[@class='m-report-lead']/p/text()").get();
        list.add(report);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws IOException {
        IresearchDetailPageProcessor processor=new IresearchDetailPageProcessor();
        Spider spider= Spider.create(processor).addUrl("https://www.analysys.cn/article/analysis/detail/20018996");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped)==0){
            processor.list.forEach(str->{
                System.out.println(str);
            });
        }

    }

}
