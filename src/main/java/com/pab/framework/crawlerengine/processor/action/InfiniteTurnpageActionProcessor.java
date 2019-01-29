//package com.pab.framework.crawlerengine.processor.action;
//
//import com.pab.framework.crawlercore.constant.Global;
//import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
//import com.pab.framework.crawlerdb.domain.CrawlerLog;
//import com.pab.framework.crawlerdb.service.DbService;
//import com.pab.framework.crawlerengine.cache.ICache;
//import com.pab.framework.crawlerengine.crawler.CrawlerHandler;
//import com.pab.framework.crawlerengine.enums.ActionTargetParamNameEnum;
//import com.pab.framework.crawlerengine.enums.ActionTargetParamTypeEnum;
//import com.pab.framework.crawlerengine.util.UrlUtil;
//import com.pab.framework.crawlerengine.model.CrawlerJobInfo;
//import com.pab.framework.crawlerengine.model.DynamicInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.apache.http.cookie.Cookie;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//
///**
// * @author xumx
// * @date 2019/1/14
// */
//@Slf4j
//@Component
//public class InfiniteTurnpageActionProcessor implements ActionProcessor{
//
//    @Autowired
//    private CrawlerHandler crawlerTurnpageHandlerImpl;
//    @Autowired
//    private DbService dbService;
//    @Autowired
//    private ICache localcache;
//
//    @Override
//    public boolean actionHandler(CrawlerActionInfo cai) {
//        String urlAddr = cai.getUrlAddr();
//        int pageStartIndex = UrlUtil.getStartPageIndex(urlAddr);
//        int pageEndIndex = UrlUtil.getEndPageIndex(urlAddr);
//        int pageIndex = pageStartIndex;
//        List<String> turnpageUrlList = new ArrayList();
//        if ((pageStartIndex < 0) || (pageEndIndex <= 0)) {
//            turnpageUrlList.add(cai.getBaseUrlAddr() + urlAddr);
//        } else {
//            while ((pageIndex >= pageStartIndex) && (pageIndex <= pageEndIndex)) {
//                if (StringUtils.isNotBlank(urlAddr)) {
//                    turnpageUrlList.add(cai.getBaseUrlAddr() + UrlUtil.getUrlWithPageIndex(urlAddr, pageIndex));
//                }
//                pageIndex++;
//            }
//        }
//        try {
//            CrawlerJobInfo cji = new CrawlerJobInfo();
//            cji.setGetUrls(turnpageUrlList);
//            cji.setActionType(Integer.valueOf(cai.getActionType()));
//            cji.setUrlType(Integer.valueOf(cai.getUrlType()));
//            cji.setRegex(cai.getCrawlerRegex());
//            cji.setCookies((List<Cookie>)(this.localcache.getCache(this.dbService.getFlowIdByActionId(cai.getActionId())
//                    +"")));
//            List<DynamicInfo> diList = (List) this.crawlerTurnpageHandlerImpl.handler(cji);
//            //可爬取的动态链接条数
//            int dynamicCount = 0;
//            if ((diList != null) && (diList.size() > 0)) {
//
//                    //插入
//                    this.dbService.updateDynamicInfos(diList, cai.getActionId(), dynamicCount);
//                    //更新milestone
//                    if(gap>0) {
//                        milestone = diList.get(0).getContent();
//                        this.dbService.saveMilestone(milestone, cai.getActionId());
//                    }
//                }
//            }
//            //日志记录
//            CrawlerLog cl = new CrawlerLog();
//            cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
//            cl.setActionId(cai.getActionId());
//            cl.setOperTime(new Date());
//            cl.setResultCode(Global.CRAWLER_RESULT_SUCCESS);
//            cl.setResultMessage("爬取翻页链接总计" + dynamicCount + "条");
//            this.dbService.saveLog(cl);
//            log.info("爬取翻页链接总计" + dynamicCount + "条");
//            return true;
//        } catch (Exception e) {
//            CrawlerLog cl = new CrawlerLog();
//            cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
//            cl.setActionId(cai.getActionId());
//            cl.setOperTime(new Date());
//            cl.setResultCode(Global.CRAWLER_RESULT_FAILURE);
//            cl.setResultMessage("爬取翻页出错:" + e.getMessage());
//            this.dbService.saveLog(cl);
//            log.error("爬取翻页出错:", e);
//        }
//        return false;
//    }
//}
