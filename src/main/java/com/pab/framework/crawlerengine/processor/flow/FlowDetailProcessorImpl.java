package com.pab.framework.crawlerengine.processor.flow;

import com.pab.framework.crawlerdb.dao.CrawlerFlowDetailDao;
import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;
import com.pab.framework.crawlerengine.processor.action.ActionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
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
   private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void process(Integer flowId) throws IOException {
        List<CrawlerFlowDetail> crawlerFlowDetails = crawlerFlowDetailDao.findAllByFlowId(flowId);
        List<Integer> actionIds=new LinkedList<>();
        for (CrawlerFlowDetail crawlerFlowDetail : crawlerFlowDetails) {
            actionIds.add(crawlerFlowDetail.getActionId());
        }
        actionProcessor.process(actionIds);

    }

}
