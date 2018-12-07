package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import java.util.List;


/**
 * @author xumx
 * @date 2018/11/16
 */
public interface ActionProcessor {

    List<String> getUrlAddrs(CrawlerActionInfo crawlerActionInfo);
    void process(Integer actionId);
}
