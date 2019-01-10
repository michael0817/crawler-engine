package com.pab.framework.crawlerengine.crawler;

import com.pab.framework.crawlerengine.vo.CrawlerJobInfo;
import us.codecraft.webmagic.processor.PageProcessor;

public interface CrawlerHandler<T> extends PageProcessor {
    T handler(CrawlerJobInfo paramCrawlerJobInfo) throws Exception;
}