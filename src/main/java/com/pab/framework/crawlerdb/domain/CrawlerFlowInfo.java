package com.pab.framework.crawlerdb.domain;

/**
 * Created by fando on 2018/11/5 17:17
 */
public class CrawlerFlowInfo {
    private int flow_id;
    private  String flow_desc;

    public int getFlow_id() {
        return flow_id;
    }

    public void setFlow_id(int flow_id) {
        this.flow_id = flow_id;
    }

    public String getFlow_desc() {
        return flow_desc;
    }

    public void setFlow_desc(String flow_desc) {
        this.flow_desc = flow_desc;
    }
}
