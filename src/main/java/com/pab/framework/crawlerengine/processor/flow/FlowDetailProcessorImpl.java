package com.pab.framework.crawlerengine.processor.flow;

import com.pab.framework.crawlerdb.dao.CrawlerFlowDetailDao;
import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;
import com.pab.framework.crawlerengine.processor.action.ActionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
public class FlowDetailProcessorImpl implements FlowDetailProcessor {
    @Autowired
    private CrawlerFlowDetailDao crawlerFlowDetailDao;

    @Autowired
    private ActionProcessor actionProcessor;

    @Override
    public void process(Integer flowId) throws IOException {
        List<CrawlerFlowDetail> crawlerFlowDetails = crawlerFlowDetailDao.findAllByFlowId(flowId);
        for (CrawlerFlowDetail crawlerFlowDetail : crawlerFlowDetails) {
             actionProcessor.process(crawlerFlowDetail.getActionId());
        }

    }

}
