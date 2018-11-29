package com.pab.framework.crawlerengine.crawler.factory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class WdzjDetail implements Detail {
    public String getTitle(Page page) {
        Html html=page.getHtml();
        String title = html.xpath("h1[@class='page-title']/text()").get();
        return title;
    }

    public String getDate(Page page) {
        Html html=page.getHtml();
        Selectable xpath = html.xpath("div[@class='page-time']");
        Document document = Jsoup.parse(xpath.get());
        return document.body().text();

    }

    public String getContent(Page page) {
        Html html=page.getHtml();
        Selectable xpath = html.xpath("div[@class='page-summary']");
        Document document = Jsoup.parse(xpath.get());
        Element body = document.body();
        Elements elements = body.getAllElements();
        int size = elements.size();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            body = elements.get(i);
            if ("div".equals(body.nodeName())) {
                builder.append(body.text());
                builder.append("\n");
            }
        }
        xpath = html.xpath("div[@class='page-content']");
        document = Jsoup.parse(xpath.get());
        body = document.body();
        elements = body.getAllElements();
        size = elements.size();
        for (int i = 0; i < size; i++) {
            body = elements.get(i);
            if ("p".equals(body.nodeName())) {
                builder.append(body.text());
                builder.append("\n");
            }
        }
        return  builder.toString();
    }


}
