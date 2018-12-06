package com.pab.framework.crawlerengine;

import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerFlowDetailDao;
import com.pab.framework.crawlerdb.dao.CrawlerFlowInfoDao;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;
import com.pab.framework.crawlerdb.domain.CrawlerFlowInfo;
import com.pab.framework.crawlerengine.processor.action.ActionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class CrawlerController {


    @Autowired
    private CrawlerFlowInfoDao crawlerFlowInfoDao;

    @Autowired
    private CrawlerFlowDetailDao crawlerFlowDetailDao;
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;

    @Autowired
    private ActionProcessor actionProcessor;

    @RequestMapping("/")
    public String index() {
        return "Hello Spring Boot 2.0!";
    }

    @RequestMapping("/run")
    public void testRun() throws InterruptedException, IOException {

        List<CrawlerFlowInfo> crawlerFlowInfos=crawlerFlowInfoDao.findAll();
        List<CrawlerFlowDetail> crawlerFlowDetails;
        CrawlerActionInfo crawlerActionInfo;
        for (CrawlerFlowInfo crawlerFlowInfo : crawlerFlowInfos) {
            int flowId=crawlerFlowInfo.getFlowId();
            crawlerFlowDetails = crawlerFlowDetailDao.findAllByFlowId( flowId );
            for (CrawlerFlowDetail crawlerFlowDetail : crawlerFlowDetails){
                int actionId = crawlerFlowDetail.getActionId();
                crawlerActionInfo = actionProcessor.findOneByActionById( actionId );
                actionProcessor.processor( crawlerActionInfo );
            }
        }
    }
}
