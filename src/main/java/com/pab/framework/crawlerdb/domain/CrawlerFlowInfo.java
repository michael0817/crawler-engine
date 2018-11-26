package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;

/**
 * Created by fando on 2018/11/5 17:17
 */
public class CrawlerFlowInfo extends BaseEntity {

    private Integer flowId;
    private  String flowDesc;

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    public String getFlowDesc() {
        return flowDesc;
    }

    public void setFlowDesc(String flowDesc) {
        this.flowDesc = flowDesc;
    }

    @Override
    public String toString() {
        return "CrawlerFlowInfo{" +
                "flowId=" + flowId +
                ", flowDesc='" + flowDesc + '\'' +
                '}';
    }
}
