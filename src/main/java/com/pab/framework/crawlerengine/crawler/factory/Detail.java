package com.pab.framework.crawlerengine.crawler.factory;

import us.codecraft.webmagic.Page;

public interface Detail {
    String getTitle(Page page);
    String getDate(Page page);
    String getContent(Page page);

}
