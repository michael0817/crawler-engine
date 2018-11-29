package com.pab.framework.crawlerengine.processor.flow;

import com.pab.framework.crawlerdb.dao.CrawlerFlowDetailDao;
import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
public class FlowDetailProcessorImpl implements FlowDetailProcessor{
    @Autowired
    private CrawlerFlowDetailDao crawlerFlowDetailDao;
    @Override
    public List<CrawlerFlowDetail> findAllByFlowId(Integer flowId) {
        return crawlerFlowDetailDao.findAllByFlowId( flowId );
    }
}
