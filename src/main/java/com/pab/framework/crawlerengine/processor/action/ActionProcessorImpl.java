package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerengine.crawler.CrawlerHandler;
import com.pab.framework.crawlerengine.dto.CrawlerJobInfo;
import com.pab.framework.crawlerengine.enums.ActionTypeEnum;
import com.pab.framework.crawlerengine.service.DbService;
import com.pab.framework.crawlerengine.util.UrlUtils;
import com.pab.framework.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
@Slf4j
public class ActionProcessorImpl implements ActionProcessor {

    @Autowired
    private CrawlerHandler crawlerTurnpageHandlerImpl;
    @Autowired
    DbService dbService;
    private ConcurrentHashMap<Integer,List<Cookie>> cookieMap = new ConcurrentHashMap();

    @Override
    public boolean process(CrawlerActionInfo cai) {
        Integer actionType = cai.getActionType();
        if (ActionTypeEnum.TURNPAGE.getLabel() == actionType) {
            return turnPageActionHandler(cai);
        }else if(ActionTypeEnum.COOKIE.getLabel() == actionType) {
            return cookieActionHandler(cai);
        }
        return false;
    }

    private boolean cookieActionHandler(CrawlerActionInfo cai) {
        Integer flowId = dbService.getFlowIdByActionId(cai.getActionId());
        List<Cookie> cookies = HttpUtil.getCookies(cai.getBaseUrlAddr());
        cookieMap.put(dbService.getFlowIdByActionId(cai.getActionId()),cookies);
        if(flowId<0||cookies==null){
            return false;
        }
        return true;
    }

    private boolean turnPageActionHandler(CrawlerActionInfo cai){
        String urlAddr = cai.getUrlAddr();
        int pageStartIndex = UrlUtils.getStartPageIndex(urlAddr);
        int pageEndIndex = UrlUtils.getEndPageIndex(urlAddr);
        int pageIndex = pageStartIndex;
        List<String> turnpageUrlList = new ArrayList();
        if (pageStartIndex < 0 || pageEndIndex <= 0) {
            turnpageUrlList.add(cai.getBaseUrlAddr() + urlAddr);
        } else {
            while (pageIndex >= pageStartIndex && pageIndex <= pageEndIndex) {
                if (StringUtils.isNotBlank(urlAddr)) {
                    turnpageUrlList.add(cai.getBaseUrlAddr() + UrlUtils.getUrlWithPageIndex(urlAddr, pageIndex));
                }
                pageIndex++;
            }
        }
        Integer actionId = dbService.getNextActionId(cai.getActionId());
        if(actionId==null || actionId < 0){
            log.error("找不到后续的Action:"+cai.getActionId());
            return false;
        }
        try {
            CrawlerJobInfo cji = new CrawlerJobInfo();
            cji.setUrls(turnpageUrlList);
            cji.setActionType(cai.getActionType());
            cji.setUrlType(cai.getUrlType());
            cji.setRegex(cai.getCrawlerRegex());
            cji.setCookies(cookieMap.get(dbService.getFlowIdByActionId(cai.getActionId())));
            List<String> content = crawlerTurnpageHandlerImpl.handler(cji);
            if (content != null && content.size() > 0) {
                dbService.updateDynamicUrls(content, actionId);
            }
            return true;
        } catch (Exception e) {
            log.error("翻页Action处理出错:", e);
            return false;
        }
    }
}