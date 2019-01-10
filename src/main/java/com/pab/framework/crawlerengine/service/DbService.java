package com.pab.framework.crawlerengine.service;

import com.pab.framework.crawlerdb.dao.*;
import com.pab.framework.crawlerdb.domain.CrawlerActionDynamicInfo;
import com.pab.framework.crawlerdb.domain.CrawlerContent;
import com.pab.framework.crawlerdb.domain.CrawlerLog;
import com.pab.framework.crawlerdb.domain.CrawlerMilestone;
import com.pab.framework.crawlerengine.vo.DynamicInfo;
import com.pab.framework.crawlerengine.vo.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void saveDynamicInfos(List<DynamicInfo> diList, int actionId, int gap) {
        Date date = new Date();
        for (int i = 0; i < (gap >= 0 ? gap : diList.size()); i++) {
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
            return this.crawlerActionInfoDao.findFlowId(actionId).intValue();
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

    public void saveCrawlerNews(List<News> newsList, int actionId, Date date) {
        for (News news : newsList) {
            CrawlerContent cc = new CrawlerContent();
            cc.setActionId(actionId);
            cc.setCrawlerDate(date);
            cc.setContent(news.toString());
            this.crawlerContentDao.save(cc);
        }
    }

    public void deleteCrawlerNewsByActionId(int actionId, Date date) {
        CrawlerContent cc = new CrawlerContent();
        cc.setActionId(actionId);
        cc.setCrawlerDate(date);
        this.crawlerContentDao.delete(cc);
    }

    @Transactional
    public void updateCrawlerNews(List<News> newsList, int actionId) {
        Date date = new Date();
        saveCrawlerNews(newsList, actionId, date);
        deleteCrawlerNewsByActionId(actionId, date);
    }

    public void saveLog(CrawlerLog cl) {
        this.crawlerLogDao.save(cl);
    }
}