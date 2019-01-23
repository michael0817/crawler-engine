package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerLog;
import com.pab.framework.crawlerengine.cache.ICache;
import com.pab.framework.crawlercore.constant.Global;
import com.pab.framework.crawlerengine.crawler.CrawlerHandler;
import com.pab.framework.crawlerengine.enums.ActionTargetParamNameEnum;
import com.pab.framework.crawlerengine.enums.ActionTargetParamTypeEnum;
import com.pab.framework.crawlerdb.service.DbService;
import com.pab.framework.crawlerengine.util.UrlUtil;
import com.pab.framework.crawlerengine.vo.CrawlerJobInfo;
import com.pab.framework.crawlerengine.vo.DynamicInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author xumx
 * @date 2019/1/14
 */
@Slf4j
@Component
public class TurnpageActionProcessor implements ActionProcessor{

    @Autowired
    private CrawlerHandler crawlerTurnpageHandlerImpl;
    @Autowired
    private DbService dbService;
    @Autowired
    private ICache localcache;

    @Override
    public boolean actionHandler(CrawlerActionInfo cai) {
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
            cji.setGetUrls(turnpageUrlList);
            cji.setActionType(Integer.valueOf(cai.getActionType()));
            cji.setUrlType(Integer.valueOf(cai.getUrlType()));
            cji.setRegex(cai.getCrawlerRegex());
            cji.setCookies((List<Cookie>)(this.localcache.getCache(this.dbService.getFlowIdByActionId(cai.getActionId())
                    +"")));
            List<DynamicInfo> diList = (List) this.crawlerTurnpageHandlerImpl.handler(cji);
            //可爬取的动态链接条数
            int dynamicCount = 0;
            if ((diList != null) && (diList.size() > 0)) {
                /**
                 * 读取上次爬取到的位置
                 * 分ALL和NEWEST两种模式，默认NEWEST
                 * NEWEST:从上一次爬取到的最后一条链接开始爬取
                 * ALL:与上一次爬取的所有链接进行去重，有更新的才爬取
                 */

                String milestone = this.dbService.getMilestoneByActionId(cai.getActionId());
                String milestoneSaveType = this.dbService.getActionTargetParamValue(cai.getActionId(),
                        ActionTargetParamTypeEnum.MILESTONE.getLabel()
                        ,ActionTargetParamNameEnum.SAVE_TYPE.getLabel());
                if(Global.CRAWLER_MILESTONE_TYPE_ALL.equalsIgnoreCase(milestoneSaveType)){
                    //全量更新，需要去重
                    List msList = Arrays.asList(milestone.split(Global.CRAWLER_MILESTONE_SPLIT));
                    if(msList.size()==1&&msList.get(0).equals("")){
                        msList = new ArrayList();
                    }
                    Set<String> msSet = new HashSet(msList);
                    Object[] array = diList.toArray();
                    Iterator<DynamicInfo> it = diList.iterator();
                    try {
                        for(int i = 0; i < array.length; i++){
                            if(!msSet.contains(diList.get(i))) {
                                if(msList.size()<=i){
                                    msList.add(((DynamicInfo)array[i]).getContent());
                                }else{
                                    msList.set(i,((DynamicInfo)array[i]).getContent());
                                }
                            }

                        }
                        while (it.hasNext()) {
                            String currContent = it.next().getContent();
                            if (msSet.contains(currContent)) {
                                it.remove();
                            }
                        }
                    }catch(ConcurrentModificationException ex){
                    }
                    dynamicCount = diList.size();
                    //插入
                    this.dbService.updateDynamicInfos(diList, cai.getActionId(), dynamicCount);
                    //更新milestone
                    if(diList.size()>0) {
                        Iterator<String> msit = msList.iterator();
                        while (msit.hasNext()) {
                            milestone += msit.next();
                            if (msit.hasNext()) {
                                milestone += Global.CRAWLER_MILESTONE_SPLIT_STR;
                            }
                        }
                        this.dbService.saveMilestone(milestone, cai.getActionId());
                    }
                }else {
                    //增量更新，判断断点
                    int gap = -1;
                    if (StringUtils.isNotBlank(milestone)) {
                        for (DynamicInfo di : diList) {
                            if (di.getContent().equalsIgnoreCase(milestone)) {
                                gap = diList.indexOf(di);
                                break;
                            }
                        }
                    }
                    dynamicCount = gap < 0 ? diList.size() : gap;
                    //插入
                    this.dbService.updateDynamicInfos(diList, cai.getActionId(), dynamicCount);
                    //更新milestone
                    if(gap>0) {
                        milestone = diList.get(0).getContent();
                        this.dbService.saveMilestone(milestone, cai.getActionId());
                    }
                }
            }
            //日志记录
            CrawlerLog cl = new CrawlerLog();
            cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
            cl.setActionId(cai.getActionId());
            cl.setOperTime(new Date());
            cl.setResultCode(Global.CRAWLER_RESULT_SUCCESS);
            cl.setResultMessage("爬取翻页链接总计" + dynamicCount + "条");
            this.dbService.saveLog(cl);
            log.info("爬取翻页链接总计" + dynamicCount + "条");
            return true;
        } catch (Exception e) {
            CrawlerLog cl = new CrawlerLog();
            cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
            cl.setActionId(cai.getActionId());
            cl.setOperTime(new Date());
            cl.setResultCode(Global.CRAWLER_RESULT_FAILURE);
            cl.setResultMessage("爬取翻页出错:" + e.getMessage());
            this.dbService.saveLog(cl);
            log.error("爬取翻页出错:", e);
        }
        return false;
    }
}
