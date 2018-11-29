package com.pab.framework.crawlerengine.crawler.factory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.3.2
 */
@Component
public class NifaDetail implements  Detail {

    public String getTitle(Page page){
        Html html=page.getHtml();
        String title=html.xpath( "td[@class='dabiaoti']/text()").get();
        return title;
    }

    @Override
    public String getDate(Page page) {
        Html html=page.getHtml();
        String date=html.xpath( "td[@class='zi8']/text()").get();
        return date;
    }

    @Override
    public String getContent(Page page) {
        Html html=page.getHtml();
        StringBuilder builder = new StringBuilder();
        Selectable xpath = html.xpath("td[@id='zhengwen']");
        Document document = Jsoup.parse(xpath.get());
        Element body = document.body();
        Elements elements = body.getAllElements();
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            body = elements.get(i);
            if ("p".equals(body.nodeName())){
                builder.append(body.text());
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
