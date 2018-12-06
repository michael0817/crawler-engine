package com.pab.framework.crawlerengine.factory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class BugutimeDetail implements Detail {

    @Override
    public String getTitle(Page page) {
        Html html=page.getHtml();
        return  html.xpath("h1[@class='view-title']/text()").get();
    }

    @Override
    public String getDate(Page page) {
        Html html=page.getHtml();
        return  html.xpath("time/text()").get();
    }

    @Override
    public String getContent(Page page) {
        Html html=page.getHtml();
        Selectable xpath = html.xpath("div[@class='view-content']");
        Document document = Jsoup.parse(xpath.get());
        Element body = document.body();
        Elements elements = body.getAllElements();
        StringBuilder builder=new StringBuilder();
        for (Element element : elements) {
            if("p".equals(element.nodeName())){
                builder.append(element.text());
                builder.append("\n");
            }
        }
        return  builder.toString();
    }
}
