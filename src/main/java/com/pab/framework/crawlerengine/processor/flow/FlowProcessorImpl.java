package com.pab.framework.crawlerengine.processor.flow;

import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerFlowDetailDao;
import com.pab.framework.crawlerdb.dao.CrawlerFlowInfoDao;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;
import com.pab.framework.crawlerdb.domain.CrawlerFlowInfo;
import com.pab.framework.crawlerdb.domain.CrawlerLog;
import com.pab.framework.crawlerengine.processor.action.ActionProcessor;
import com.pab.framework.crawlerengine.service.DbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
@Slf4j
public class FlowProcessorImpl implements FlowProcessor {

    @Autowired
    private CrawlerFlowInfoDao crawlerFlowInfoDao;
    @Autowired
    private CrawlerFlowDetailDao crawlerFlowDetailDao;
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;
    @Autowired
    private DbService dbService;
    @Autowired
    private ActionProcessor actionProcessorImpl;

    public void run() {
        List<CrawlerFlowInfo> cfis = this.crawlerFlowInfoDao.findAll();
        for (CrawlerFlowInfo cfi : cfis) {
            if (!"OFF".equalsIgnoreCase(cfi.getFlowSchedule())) {
                boolean result = false;
                List<CrawlerFlowDetail> cfds = this.crawlerFlowDetailDao.findAllByFlowId(cfi.getFlowId());
                for (CrawlerFlowDetail cfd : cfds) {
                    CrawlerActionInfo cai = this.crawlerActionInfoDao.findCrawlerActionInfo(cfd.getActionId());
                    result = this.actionProcessorImpl.process(cai);
                    if (!result) {
                        log.error("爬虫任务执行失败:" + cfi.getFlowDesc());
                        CrawlerLog cl = new CrawlerLog();
                        cl.setFlowId(cfi.getFlowId());
                        cl.setActionId(-9999);
                        cl.setOperTime(new Date());
                        cl.setResultCode("F");
                        cl.setResultMessage(cfi.getFlowDesc() + "执行失败");
                        this.dbService.saveLog(cl);
                    }
                }
                if (result == true) {
                    log.info("爬虫任务执行成功:" + cfi.getFlowDesc());
                    CrawlerLog cl = new CrawlerLog();
                    cl.setFlowId(cfi.getFlowId());
                    cl.setActionId(-9999);
                    cl.setOperTime(new Date());
                    cl.setResultCode("S");
                    cl.setResultMessage(cfi.getFlowDesc() + "执行成功");
                    this.dbService.saveLog(cl);
                }
            }
        }
    }
}