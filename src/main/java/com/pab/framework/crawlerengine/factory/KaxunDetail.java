package com.pab.framework.crawlerengine.factory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

/**
 * @author code4crafter@gmail.com <br>
 * @since 0.3.2
 */
@Component
public class KaxunDetail implements Detail {

    public String getTitle(Page page) {
        Html html=page.getHtml();
        String title = html.xpath("h1[@class='nr01_tit']/text()").get();
        return title;
    }

    @Override
    public String getDate(Page page) {
        Html html=page.getHtml();
        String date = html.xpath("p[@class='right01_date']/text(2)").get();
        return date;
    }

    @Override
    public String getContent(Page page) {
        Html html=page.getHtml();
        Selectable xpath = html.xpath("div[@class='right01_nr']");
        Document document = Jsoup.parse(xpath.get());
        Element body = document.body();
        String string = body.text();
        String[] strings = string.split(" ");
        StringBuilder builder = new StringBuilder(string.length());
        int length = strings.length;
        for (int i = 0; i < length; i++) {
            builder.append(strings[i]);
            builder.append("\n");
        }
        return builder.toString();
    }
}
