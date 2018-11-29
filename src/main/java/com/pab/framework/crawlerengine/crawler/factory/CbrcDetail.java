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
public class CbrcDetail implements  Detail {

    public String getTitle(Page page){
        Html html=page.getHtml();
        return html.xpath( "div[@id='docTitle']/div/text()").get();
    }

    @Override
    public String getDate(Page page) {

        return null;
    }

    @Override
    public String getContent(Page page) {
        Html html=page.getHtml();
        Selectable xpath =  html.xpath("div[@class='Section0']");
        Document document = Jsoup.parse(xpath.get());
        Element body =document.body();
        Elements elements = body.getAllElements();
        StringBuilder builder=new StringBuilder();
        String string;
        for (Element element : elements) {
            if ("p".equals(element.nodeName())){
                elements = element.getAllElements();
                for (Element element1 : elements) {
                    if ("span".equals(element1.nodeName())){
                        string=element1.text();
                        if (string!=null){
                            builder.append(string);
                        }
                    }
                }
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
