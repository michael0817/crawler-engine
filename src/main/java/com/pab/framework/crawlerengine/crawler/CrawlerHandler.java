package com.pab.framework.crawlerengine.crawler;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;

import java.io.IOException;
import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
public interface CrawlerHandler {
    void handler(CrawlerActionInfo crawlerActionInfo,List<String> urlAddrs) throws IOException;

}
