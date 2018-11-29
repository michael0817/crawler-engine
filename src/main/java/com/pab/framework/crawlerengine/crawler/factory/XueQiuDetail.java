package com.pab.framework.crawlerengine.crawler.factory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class XueQiuDetail implements Detail {


    @Override
    public String getTitle(Page page) {
        Html html=page.getHtml();
        return html.xpath("h1[@class='article__bd__title']/text()").get();
    }

    @Override
    public String getDate(Page page) {
        Html html=page.getHtml();
        Selectable xpath = html.xpath("div[@class='avatar__subtitle']");
        Document document= Jsoup.parse(xpath.get());
        Element body = document.body();
        Elements elements = body.getAllElements();
        StringBuilder builder=new StringBuilder();
        for (Element element : elements) {
            if ("span".equals(element.nodeName())){
                builder.append(element.text());
                builder.append(" ");
            }
            else if("a".equals(element.nodeName())){
                builder.append(element.text());
            }
        }
        return builder.toString();
    }

    @Override
    public String getContent(Page page) {
        Html html=page.getHtml();
        Selectable xpath = html.xpath("div[@class='article__bd__detail']/p/text()");
        List<Selectable> nodes = xpath.nodes();
        StringBuilder builder=new StringBuilder();
        for (Selectable node : nodes) {
            builder.append(node.get());
            builder.append("\n");
        }
        return builder.toString();
    }
}
