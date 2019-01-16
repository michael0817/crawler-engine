package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerLog;
import com.pab.framework.crawlerengine.cache.ICache;
import com.pab.framework.crawlerengine.constant.Global;
import com.pab.framework.crawlerengine.crawler.CrawlerHandler;
import com.pab.framework.crawlerengine.service.DbService;
import com.pab.framework.crawlerengine.vo.CrawlerJobInfo;
import com.pab.framework.crawlerengine.vo.News;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
public class NewsActionProcessor implements ActionProcessor{

    @Autowired
    private CrawlerHandler crawlerNewsHandlerImpl;
    @Autowired
    private DbService dbService;
    @Autowired
    private ICache localcache;

    @Override
    public boolean actionHandler(CrawlerActionInfo cai) {
        try {
            int preActionId = Integer.parseInt(cai.getUrlAddr().substring(cai
                    .getUrlAddr().indexOf(Global.DYNAMIC_ACTION_PREFIX) + 1, cai
                    .getUrlAddr().indexOf(Global.DYNAMIC_ACTION_SUBFIX)));

            CrawlerJobInfo cji = new CrawlerJobInfo();
            List<String> urlList = this.dbService.getDynamicContentsByActionId(preActionId);
            if (urlList.size() == 0) {
                CrawlerLog cl = new CrawlerLog();
                cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
                cl.setActionId(cai.getActionId());
                cl.setOperTime(new Date());
                cl.setResultCode(Global.CRAWLER_RESULT_SUCCESS);
                cl.setResultMessage("无可爬取新闻");
                this.dbService.saveLog(cl);
                log.info("无可爬取新闻");
                return true;
            }
            //补全URL
            for (int i = 0; i < urlList.size(); i++) {
                if ((StringUtils.isNotBlank(urlList.get(i))) && (urlList.get(i).length() >= 4) &&
                        (!"http".equalsIgnoreCase(urlList.get(i).substring(0, 4))) &&
                        (!urlList.get(i).contains(cai.getBaseUrlAddr()))) {
                    urlList.set(i, cai.getBaseUrlAddr() + (urlList.get(i).startsWith("/") ? "" : "/") + urlList.get(i));
                }
            }
            cji.setUrls(urlList);
            cji.setActionType(cai.getActionType());
            cji.setUrlType(cai.getUrlType());
            cji.setRegex(cai.getCrawlerRegex());
            cji.setCookies((List<Cookie>)(this.localcache.getCache(this.dbService.getFlowIdByActionId(cai.getActionId())
                    +"")));
            List<News> newsList = (List)this.crawlerNewsHandlerImpl.handler(cji);
            if ((newsList != null) && (newsList.size() > 0)) {
                this.dbService.updateCrawlerNews(newsList, cai.getActionId());
            }
            //日志记录
            CrawlerLog cl = new CrawlerLog();
            if (urlList.size() == newsList.size()) {
                cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
                cl.setActionId(cai.getActionId());
                cl.setOperTime(new Date());
                cl.setResultCode(Global.CRAWLER_RESULT_SUCCESS);
                cl.setResultMessage("爬取新闻总计" + newsList.size() + "条");
                log.info("爬取新闻总计" + newsList.size() + "条");
            } else {
                cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
                cl.setActionId(cai.getActionId());
                cl.setOperTime(new Date());
                cl.setResultCode(Global.CRAWLER_RESULT_WARN);
                cl.setResultMessage("爬取新闻总计" + newsList.size() + "条,失败" + (urlList.size() - newsList.size()) + "条");
                log.warn("爬取新闻总计" + newsList.size() + "条,失败" + (urlList.size() - newsList.size()) + "条");
            }
            this.dbService.saveLog(cl);
            return true;
        } catch (Exception e) {
            log.error("爬取新闻出错,actionId:" + cai.getActionId(), e);
            CrawlerLog cl = new CrawlerLog();
            cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
            cl.setActionId(cai.getActionId());
            cl.setOperTime(new Date());
            cl.setResultCode(Global.CRAWLER_RESULT_FAILURE);
            cl.setResultMessage("爬取新闻出错:" + e.getMessage());
            this.dbService.saveLog(cl);
            log.error("爬取新闻出错:" + e.getMessage());
        }
        return false;
    }
}
