package com.pab.framework.crawlerengine.crawler.factory;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

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
        Html html = page.getHtml();
        Document document = html.getDocument();
        Element body = document.body();
        body = body.child(0).child(1);
        List<Element> elements = body.getElementsByTag("p");
        int size = elements.size();
        Element element;
        List<Element> spans;
        StringBuilder builder = new StringBuilder();
        String text;
        for (int i = 0; i < size; i++) {
            element = elements.get(i);
            spans = element.getElementsByTag("span");
            for (int j = 0; j < spans.size(); j++) {
                text = spans.get(j).text().trim();
                if (!text.isEmpty()) {
                    builder.append(text);
                }
            }
            builder.append("\n");

        }

        return builder.toString();
    }
}
