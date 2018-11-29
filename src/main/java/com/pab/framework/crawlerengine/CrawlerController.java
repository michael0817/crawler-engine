package com.pab.framework.crawlerengine;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;
import com.pab.framework.crawlerdb.domain.CrawlerFlowInfo;
import com.pab.framework.crawlerengine.processor.action.ActionProcessor;
import com.pab.framework.crawlerengine.processor.flow.FlowDetailProcessor;
import com.pab.framework.crawlerengine.processor.flow.FlowProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.List;

@RestController
public class CrawlerController {

    @Autowired
    private FlowProcessor flowProcessor;
    @Autowired
    private FlowDetailProcessor flowDetailProcessor;
    @Autowired
    private ActionProcessor actionProcessor;

    @RequestMapping("/")
    public String index() {
        return "Hello Spring Boot 2.0!";
    }

    @RequestMapping("/run")
    public void testRun() throws InterruptedException, MalformedURLException {
        List<CrawlerFlowInfo> crawlerFlowInfos = flowProcessor.findAll();
        List<CrawlerFlowDetail> crawlerFlowDetails;
        CrawlerActionInfo crawlerActionInfo;
        for (CrawlerFlowInfo crawlerFlowInfo : crawlerFlowInfos) {
            int flowId = crawlerFlowInfo.getFlowId();
            crawlerFlowDetails = flowDetailProcessor.findAllByFlowId( flowId );
            for (CrawlerFlowDetail crawlerFlowDetail : crawlerFlowDetails) {
                int actionId = crawlerFlowDetail.getActionId();
                crawlerActionInfo = actionProcessor.findOneByActionById( actionId );
                actionProcessor.processor( crawlerActionInfo );
            }
        }
    }
}
