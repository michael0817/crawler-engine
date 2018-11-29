package com.pab.framework.crawlerengine.processor.flow;

import com.pab.framework.crawlerdb.domain.CrawlerFlowInfo;
import com.pab.framework.crawlerengine.processor.BaseProcessor;

import java.util.List;

/**
 *
 * @author xumx
 * @date 2018/11/16
 */
public interface FlowProcessor extends BaseProcessor {
   List<CrawlerFlowInfo> findAll();
}
