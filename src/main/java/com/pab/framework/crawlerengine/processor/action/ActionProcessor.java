package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author xumx
 * @date 2018/11/16
 */
public interface ActionProcessor {

    List<String> getUrls(List<CrawlerActionInfo> crawlerActionInfos, Map<Integer,List<String>> urlAddrs) throws IOException;
    Map<Integer,List<String>> getUrlAddrs(List<CrawlerActionInfo> crawlerActionInfos);
    void process(List<Integer> actionIds) throws IOException;
}
