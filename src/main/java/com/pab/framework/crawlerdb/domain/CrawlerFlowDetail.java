package com.pab.framework.crawlerdb.domain;

/**
 * Created by fando on 2018/11/5 17:26
 */
public class CrawlerFlowDetail {
    private Integer flowId;
    private Integer actionId;
    private Integer orderNum;

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
