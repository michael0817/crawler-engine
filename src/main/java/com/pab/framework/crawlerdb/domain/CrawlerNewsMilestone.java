package com.pab.framework.crawlerdb.domain;

import java.util.Date;

/**
 * Created by fando on 2018/11/4 18:00
 */
public class CrawlerNewsMilestone {
    private  Integer actionId;
    private String urlAddr;
    private Date crawlerDate;

    public CrawlerNewsMilestone() {
    }

    public CrawlerNewsMilestone(Integer actionId, String urlAddr) {
        this.actionId = actionId;
        this.urlAddr = urlAddr;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public String getUrlAddr() {
        return urlAddr;
    }

    public void setUrlAddr(String urlAddr) {
        this.urlAddr = urlAddr;
    }

    public Date getCrawlerDate() {
        return crawlerDate;
    }

    public void setCrawlerDate(Date crawlerDate) {
        this.crawlerDate = crawlerDate;
    }
}
