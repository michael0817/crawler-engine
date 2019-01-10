package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerLog;
import com.pab.framework.crawlerengine.crawler.CrawlerHandler;
import com.pab.framework.crawlerengine.enums.ActionTypeEnum;
import com.pab.framework.crawlerengine.service.DbService;
import com.pab.framework.crawlerengine.service.PdfService;
import com.pab.framework.crawlerengine.util.HttpUtil;
import com.pab.framework.crawlerengine.util.UrlUtil;
import com.pab.framework.crawlerengine.vo.CrawlerJobInfo;
import com.pab.framework.crawlerengine.vo.DynamicInfo;
import com.pab.framework.crawlerengine.vo.News;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Slf4j
public class ActionProcessorImpl implements ActionProcessor {

    @Autowired
    private CrawlerHandler crawlerTurnpageHandlerImpl;

    @Autowired
    private CrawlerHandler crawlerNewsHandlerImpl;

    @Autowired
    private DbService dbService;
    @Autowired
    private PdfService pdfService;
    private ConcurrentHashMap<Integer, List<Cookie>> cookieMap = new ConcurrentHashMap();

    public boolean process(CrawlerActionInfo cai) {
        Integer actionType = Integer.valueOf(cai.getActionType());
        if (ActionTypeEnum.TURNPAGE.getLabel() == actionType)
            return turnPageActionHandler(cai);
        if (ActionTypeEnum.COOKIE.getLabel() == actionType)
            return cookieActionHandler(cai);
        if (ActionTypeEnum.NEWS.getLabel() == actionType) {
            return newsActionHandler(cai);
        }
        return false;
    }

    private boolean cookieActionHandler(CrawlerActionInfo cai) {
        Integer flowId = Integer.valueOf(this.dbService.getFlowIdByActionId(cai.getActionId()));
        List<Cookie> cookies = HttpUtil.getCookies(cai.getBaseUrlAddr());
        this.cookieMap.put(Integer.valueOf(this.dbService.getFlowIdByActionId(cai.getActionId())), cookies);
        return (flowId.intValue() >= 0) && (cookies != null);
    }

    private boolean turnPageActionHandler(CrawlerActionInfo cai) {
        String urlAddr = cai.getUrlAddr();
        int pageStartIndex = UrlUtil.getStartPageIndex(urlAddr);
        int pageEndIndex = UrlUtil.getEndPageIndex(urlAddr);
        int pageIndex = pageStartIndex;
        List<String> turnpageUrlList = new ArrayList();
        if ((pageStartIndex < 0) || (pageEndIndex <= 0)) {
            turnpageUrlList.add(cai.getBaseUrlAddr() + urlAddr);
        } else {
            while ((pageIndex >= pageStartIndex) && (pageIndex <= pageEndIndex)) {
                if (StringUtils.isNotBlank(urlAddr)) {
                    turnpageUrlList.add(cai.getBaseUrlAddr() + UrlUtil.getUrlWithPageIndex(urlAddr, pageIndex));
                }
                pageIndex++;
            }
        }
        try {
            CrawlerJobInfo cji = new CrawlerJobInfo();
            cji.setUrls(turnpageUrlList);
            cji.setActionType(Integer.valueOf(cai.getActionType()));
            cji.setUrlType(Integer.valueOf(cai.getUrlType()));
            cji.setRegex(cai.getCrawlerRegex());
            cji.setCookies(this.cookieMap.get(Integer.valueOf(this.dbService.getFlowIdByActionId(cai.getActionId()))));
            List<DynamicInfo> diList = (List) this.crawlerTurnpageHandlerImpl.handler(cji);
            if ((diList != null) && (diList.size() > 0)) {
                String milestone = this.dbService.getMilestoneByActionId(cai.getActionId());
                int gap = -1;
                if (StringUtils.isNotBlank(milestone)) {
                    for (DynamicInfo di : diList) {
                        if (di.getContent().equalsIgnoreCase(milestone)) {
                            gap = diList.indexOf(di);
                            break;
                        }
                    }
                }
                this.dbService.updateDynamicInfos(diList, cai.getActionId(), gap);
                CrawlerLog cl = new CrawlerLog();
                cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
                cl.setActionId(cai.getActionId());
                cl.setOperTime(new Date());
                cl.setResultCode("S");
                cl.setResultMessage("爬取翻页链接总计" + (gap < 0 ? diList.size() : gap) + "条");
                this.dbService.saveLog(cl);
            }
            return true;
        } catch (Exception e) {
            log.error("爬取翻页出错,actionId:" + cai.getActionId(), e);
            CrawlerLog cl = new CrawlerLog();
            cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
            cl.setActionId(cai.getActionId());
            cl.setOperTime(new Date());
            cl.setResultCode("F");
            cl.setResultMessage("爬取翻页出错:" + e.getMessage());
            this.dbService.saveLog(cl);
        }
        return false;
    }


    private boolean newsActionHandler(CrawlerActionInfo cai) {
        try {
            int preActionId = Integer.parseInt(cai
                    .getUrlAddr().substring(cai
                            .getUrlAddr().indexOf("[") + 1, cai
                            .getUrlAddr().indexOf("]")));

            CrawlerJobInfo cji = new CrawlerJobInfo();
            List<String> urlList = this.dbService.getDynamicContentsByActionId(preActionId);
            if (urlList.size() == 0) {
                log.info("无可爬取新闻,actionId:" + cai.getActionId());
                CrawlerLog cl = new CrawlerLog();
                cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
                cl.setActionId(cai.getActionId());
                cl.setOperTime(new Date());
                cl.setResultCode("S");
                cl.setResultMessage("爬取新闻总计0条");
                this.dbService.saveLog(cl);
                return true;
            }
            String milestone = urlList.get(0);
            for (int i = 0; i < urlList.size(); i++) {
                if ((StringUtils.isNotBlank(urlList.get(i))) && (urlList.get(i).length() >= 4) &&
                        (!"http".equalsIgnoreCase(urlList.get(i).substring(0, 4))) &&
                        (!urlList.get(i).contains(cai.getBaseUrlAddr()))) {
                    urlList.set(i, cai.getBaseUrlAddr() + (urlList.get(i).startsWith("/") ? "" : "/") + urlList.get(i));
                }
            }


            cji.setUrls(urlList);
            cji.setActionType(Integer.valueOf(cai.getActionType()));
            cji.setUrlType(Integer.valueOf(cai.getUrlType()));
            cji.setRegex(cai.getCrawlerRegex());
            cji.setCookies(this.cookieMap.get(Integer.valueOf(this.dbService.getFlowIdByActionId(cai.getActionId()))));
            List<News> newsList = (List) this.crawlerNewsHandlerImpl.handler(cji);
            if ((newsList != null) && (newsList.size() > 0)) {
                this.dbService.updateCrawlerNews(newsList, cai.getActionId());
                this.pdfService.generateNewsFile(cai.getActionId(), newsList);
            }
            this.dbService.setMilestone(milestone, preActionId);
            CrawlerLog cl = new CrawlerLog();
            if (urlList.size() == newsList.size()) {
                cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
                cl.setActionId(cai.getActionId());
                cl.setOperTime(new Date());
                cl.setResultCode("S");
                cl.setResultMessage("爬取新闻总计" + newsList.size() + "条");
            } else {
                cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
                cl.setActionId(cai.getActionId());
                cl.setOperTime(new Date());
                cl.setResultCode("W");
                cl.setResultMessage("爬取新闻总计" + newsList.size() + "条,失败" + (urlList.size() - newsList.size()) + "条");
            }
            this.dbService.saveLog(cl);
            return true;
        } catch (Exception e) {
            log.error("爬取新闻出错,actionId:" + cai.getActionId(), e);
            CrawlerLog cl = new CrawlerLog();
            cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
            cl.setActionId(cai.getActionId());
            cl.setOperTime(new Date());
            cl.setResultCode("F");
            cl.setResultMessage("爬取新闻出错:" + e.getMessage());
            this.dbService.saveLog(cl);
        }
        return false;
    }
}