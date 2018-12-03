package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;

import java.io.IOException;

/**
 * @author xumx
 * @date 2018/11/16
 */
public interface ActionProcessor {
    void processor(CrawlerActionInfo crawlerActionInfo) throws InterruptedException, IOException;
    CrawlerActionInfo findOneByActionById(Integer actionId);

}
