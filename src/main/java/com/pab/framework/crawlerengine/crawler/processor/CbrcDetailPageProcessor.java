package com.pab.framework.crawlerengine.crawler.processor;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.3.2
 */
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
        docTitle= StringEscapeUtils.unescapeHtml(docTitle);
        docTitle=StringUtils.trimToNull(docTitle);
        if (docTitle!=null){
            list.add(docTitle);
        }
        Document document = html.getDocument();
        Elements p$s = document.getElementsByAttributeValueMatching("class","p\\d+");
        int size = p$s.size();
        Element p$;
        Elements msos;
        Element mso;
        int length;
        String text;
        StringBuilder builder=null;
        for (int i = 0; i < size; i++) {
            p$ = p$s.get(i);
            msos=p$.children();
            length= msos.size();
            builder=new StringBuilder();
            for (int j = 0; j < length; j++) {
                mso= msos.get(j);
                text=mso.text();
                text=StringEscapeUtils.unescapeHtml(text);
                text= StringUtils.trimToNull(text);
                if (text!=null){
                    builder.append(text);
                    builder.append(" ");
                }
            }
            list.add(builder.toString());
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

}
