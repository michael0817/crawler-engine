package com.pab.framework.crawlerengine.processor.flow;

import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;
import com.pab.framework.crawlerengine.processor.BaseProcessor;

import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
public interface FlowDetailProcessor extends BaseProcessor {
    List<CrawlerFlowDetail>  findAllByFlowId(Integer flowId);
}
