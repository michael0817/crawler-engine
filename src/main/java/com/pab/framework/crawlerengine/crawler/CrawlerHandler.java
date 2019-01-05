package com.pab.framework.crawlerengine.crawler;

import com.pab.framework.crawlerengine.dto.CrawlerJobInfo;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
public interface CrawlerHandler extends PageProcessor {

    List<String> handler(CrawlerJobInfo crawlerJobInfo) throws Exception;

}
