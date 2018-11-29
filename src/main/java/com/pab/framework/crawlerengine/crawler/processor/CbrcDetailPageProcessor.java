package com.pab.framework.crawlerengine.crawler.processor;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
 * @since 0.3.2
 */
@Component
public class CbrcDetailPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private List<String> list=new ArrayList<String>();

    public List<String> getList() {
        return list;
    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        Html html = page.getHtml();
        String regex = "http://www.miibeian.gov.cn";
        Selectable xpath = html.links().regex(regex);
        if (xpath.match()) {
            page.setSkip(true);
        }
        String docTitle = html.xpath("div[@id='docTitle']/div/text()").get();
        docTitle=StringUtils.trimToNull(docTitle);
        if (docTitle!=null){
            docTitle= StringEscapeUtils.unescapeHtml(docTitle);
            list.add(docTitle);
        }
        xpath= html.xpath( "div[@class='Section0']/p" );
        List<Selectable> nodes = xpath.nodes();
        String string;
        for (Selectable node : nodes) {
            string=node.replace("<[^<>]+>","").get();
            string=StringEscapeUtils.unescapeHtml( string );
            string=string.trim();
            if (!string.isEmpty()){
                list.add(string);
            }
        }
    }

    public void process(String url){
        Spider spider= Spider.create(this).addUrl(url);
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped)==0){
            list.forEach( s->{
                System.out.println(s);
            } );
        }
    }
    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws IOException {
        CbrcDetailPageProcessor processor=new CbrcDetailPageProcessor();
        Spider spider= Spider.create(processor).addUrl("http://www.cbrc.gov.cn/chinese/newShouDoc/6AE01C768AE54014B66A390E37CB9E6D.html");
        spider.run();
        if (spider.getStatus().compareTo(Spider.Status.Stopped)==0){
          processor.list.forEach( s->{
              System.out.println(s);
          } );
        }

    }

}
