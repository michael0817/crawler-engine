package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;


/**
 * @author xumx
 * @date 2018/11/16
 */
public interface ActionProcessor {

    //    List<String> getUrls(List<CrawlerActionInfoDto> crawlerActionInfos, Map<Integer,List<String>> urlAddrs) throws IOException;
//    Map<Integer,List<String>> getUrlAddrs(List<CrawlerActionInfoDto> crawlerActionInfos);
    boolean actionHandler(CrawlerActionInfo crawlerActionInfo);
}
