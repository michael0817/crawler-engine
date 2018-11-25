package com.pab.framework.crawlerdb.domain;

/**
 * Created by fando on 2018/11/3 13:23
 */
public class CrawlerActionInfo {
    private Integer actionId;
    private String actionDesc;
    private Integer actionType;
    private Integer urlType;
    private String urlAddr;
    private String baseUrlAddr;
    private String crawlerRegex;

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Integer getUrlType() {
        return urlType;
    }

    public void setUrlType(Integer urlType) {
        this.urlType = urlType;
    }

    public String getUrlAddr() {
        return urlAddr;
    }

    public void setUrlAddr(String urlAddr) {
        this.urlAddr = urlAddr;
    }

    public String getBaseUrlAddr() {
        return baseUrlAddr;
    }

    public void setBaseUrlAddr(String baseUrlAddr) {
        this.baseUrlAddr = baseUrlAddr;
    }

    public String getCrawlerRegex() {
        return crawlerRegex;
    }

    public void setCrawlerRegex(String crawlerRegex) {
        this.crawlerRegex = crawlerRegex;
    }
}
