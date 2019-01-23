package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerLog;
import com.pab.framework.crawlerengine.cache.ICache;
import com.pab.framework.crawlercore.constant.Global;
import com.pab.framework.crawlerdb.service.DbService;
import com.pab.framework.crawlerengine.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author xumx
 * @date 2019/1/14
 */
@Slf4j
@Component
public class CookieActionProcessor implements ActionProcessor{

    @Autowired
    private DbService dbService;
    @Autowired
    private ICache localcache;

    @Override
    public boolean actionHandler(CrawlerActionInfo cai) {
        try {
            Integer flowId = Integer.valueOf(this.dbService.getFlowIdByActionId(cai.getActionId()));
            List<Cookie> cookies = HttpUtil.getCookies(cai.getBaseUrlAddr());
            this.localcache.setCache(this.dbService.getFlowIdByActionId(cai.getActionId()) + "", cookies);
            //日志记录
            CrawlerLog cl = new CrawlerLog();
            cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
            cl.setActionId(cai.getActionId());
            cl.setOperTime(new Date());
            cl.setResultCode(Global.CRAWLER_RESULT_SUCCESS);
            cl.setResultMessage("爬取Cookie成功");
            this.dbService.saveLog(cl);
            log.info("爬取Cookie成功");
            return (flowId.intValue() >= 0) && (cookies != null);
        } catch (Exception e) {
            CrawlerLog cl = new CrawlerLog();
            cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
            cl.setActionId(cai.getActionId());
            cl.setOperTime(new Date());
            cl.setResultCode(Global.CRAWLER_RESULT_FAILURE);
            cl.setResultMessage("爬取Cookie出错:" + e.getMessage());
            this.dbService.saveLog(cl);
            log.error("爬取Cookie出错", e);
        }
        return false;
    }
}
