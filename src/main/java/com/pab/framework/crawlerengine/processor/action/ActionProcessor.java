package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;

import java.net.MalformedURLException;

/**
 * @author xumx
 * @date 2018/11/16
 */
public interface ActionProcessor {
    void processor(CrawlerActionInfo crawlerActionInfo) throws InterruptedException, MalformedURLException;
    CrawlerActionInfo findOneByActionById(Integer actionId);

}
