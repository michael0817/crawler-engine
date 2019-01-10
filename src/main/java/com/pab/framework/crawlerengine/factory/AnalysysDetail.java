package com.pab.framework.crawlerengine.factory;


import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

public class AnalysysDetail implements Detail {


    @Override
    public String getTitle(Page page) {
        Html html = page.getHtml();
        return html.xpath("div[@class='left_title']/h1/text()").get();
    }

    @Override
    public String getDate(Page page) {
        Html html = page.getHtml();
        return html.xpath("p[@id='anal_icon']/span[3]/text()").get();
    }

    @Override
    public String getContent(Page page) {
        Html html = page.getHtml();
        return html.xpath("div[@class='summary']/text()").get();
    }
}
