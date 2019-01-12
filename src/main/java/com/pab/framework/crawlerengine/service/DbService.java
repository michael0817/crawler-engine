package com.pab.framework.crawlerengine.service;

import com.alibaba.fastjson.JSON;
import com.pab.framework.crawlerdb.dao.*;
import com.pab.framework.crawlerdb.domain.*;
import com.pab.framework.crawlerengine.vo.DynamicInfo;
import com.pab.framework.crawlerengine.vo.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class DbService {

    @Autowired
    private CrawlerActionDynamicInfoDao crawlerActionDynamicInfoDao;
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;
    @Autowired
    private CrawlerFlowInfoDao crawlerFlowInfoDao;
    @Autowired
    private CrawlerFlowDetailDao crawlerFlowDetailDao;
    @Autowired
    private CrawlerMilestoneDao crawlerMilestoneDao;
    @Autowired
    private CrawlerContentDao crawlerContentDao;
    @Autowired
    private CrawlerLogDao crawlerLogDao;
    @Autowired
    private CrawlerActionTargetParamDao crawlerActionTargetParamDao;

    @Transactional
    public void saveDynamicInfos(List<DynamicInfo> diList, int actionId, int gap) {
        Date date = new Date();
        for (int i = 0; i < gap; i++) {
            CrawlerActionDynamicInfo cadi = new CrawlerActionDynamicInfo();
            cadi.setActionId(actionId);
            cadi.setArticle((diList.get(i)).getArticle());
            cadi.setContent((diList.get(i)).getContent());
            cadi.setOrderNum(i + 1);
            cadi.setOperTime(date);
            this.crawlerActionDynamicInfoDao.save(cadi);
        }
    }

    public void deleteDynamicInfosByActionId(int actionId) {
        this.crawlerActionDynamicInfoDao.deleteAll(actionId);
    }

    @Transactional
    public void updateDynamicInfos(List<DynamicInfo> content, int actionId, int gap) {
        deleteDynamicInfosByActionId(actionId);
        saveDynamicInfos(content, actionId, gap);
    }


    public int getFlowIdByActionId(int actionId) {
        try {
            return this.crawlerFlowDetailDao.findFlowIdByActionId(actionId);
        } catch (Exception e) {
            log.error("SQL执行异常", e);
        }

        return -1;
    }

    public List<String> getDynamicContentsByActionId(int actionId) {
        try {
            return this.crawlerActionDynamicInfoDao.findDynamicContents(actionId);
        } catch (Exception e) {
            log.error("SQL执行异常", e);
        }

        return null;
    }

    public String getFlowNameByActionId(int actionId) {
        try {
            int flowId = this.crawlerFlowDetailDao.findFlowIdByActionId(actionId);
            return this.crawlerFlowInfoDao.findOne(flowId).getFlowDesc();
        } catch (Exception e) {
            log.error("SQL执行异常", e);
        }
        return null;
    }

    public String getMilestoneByActionId(int actionId) {
        try {
            return this.crawlerMilestoneDao.findMilestoneByActionId(actionId);
        } catch (Exception e) {
            log.error("SQL执行异常", e);
        }
        return null;
    }

    public void setMilestone(String milestone, int actionId) {
        try {
            CrawlerMilestone cm = new CrawlerMilestone();
            cm.setActionId(actionId);
            cm.setCrawlerDate(new Date());
            cm.setMilestone(milestone);
            this.crawlerMilestoneDao.save(cm);
        } catch (Exception e) {
            log.error("SQL执行异常", e);
        }
    }

    public void saveCrawlerNews(List<News> newsList, int actionId, LocalDate date) {
        for (News news : newsList) {
            CrawlerContent cc = new CrawlerContent();
            cc.setActionId(actionId);
            cc.setCrawlerDate(date);
            cc.setContent(JSON.toJSONString(news));
            this.crawlerContentDao.save(cc);
        }
    }

    @Transactional
    public void updateCrawlerNews(List<News> newsList, int actionId) {
        LocalDate date = LocalDate.now();
        saveCrawlerNews(newsList, actionId, date);
        deleteContentByActionIdAndCrawlerDate(actionId,date.minusMonths(1));
        deleteContentByActionIdAndCrawlerDate(actionId,date.minusMonths(2));
        deleteContentByActionIdAndCrawlerDate(actionId,date.minusMonths(3));


    }

    public void deleteContentByActionIdAndCrawlerDate(int actionId, LocalDate crawlerDate) {
        this.crawlerContentDao.deleteByActionIdAndCrawlerDate(actionId, crawlerDate);

    }

    public List<CrawlerContent> getContentByActionIdAndCrawlerDate(int actionId, LocalDate crawlerDate) {
        return this.crawlerContentDao.findByActionIdAndCrawlerDate(actionId, crawlerDate);
    }

    public void saveLog(CrawlerLog cl) {
        this.crawlerLogDao.save(cl);
    }

    public String getActionTargetParamValue(int actionId, String paramType, String paramName){
        CrawlerActionTargetParam catp = new CrawlerActionTargetParam();
        catp.setActionId(actionId);
        catp.setParamType(paramType);
        catp.setParamName(paramName);
        return crawlerActionTargetParamDao.findParamValue(catp);
    }

    public List<Integer> getActionIdByActionType(int actionType){
        return crawlerActionInfoDao.findByActionType(actionType);
    }
}