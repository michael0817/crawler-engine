package com.pab.framework.crawlerdb.domain;

/**
 * Created by fando on 2018/11/4 17:00
 */
public class CrawActionDynamicInfo {

    private Integer dynActionId;
    private Integer actionId;
    private Integer actionType;
    private Integer urlType;
    private String  urlAddr;

    public Integer getDynActionId() {
        return dynActionId;
    }

    public void setDynActionId(Integer dynActionId) {
        this.dynActionId = dynActionId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
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
}
