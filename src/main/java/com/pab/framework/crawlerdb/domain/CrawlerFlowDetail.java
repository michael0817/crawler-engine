package com.pab.framework.crawlerdb.domain;

/**
 * Created by fando on 2018/11/5 17:26
 */
public class CrawlerFlowDetail {
    private int flow_id;
    private int action_id;
    private int order_num;

    public int getFlow_id() {
        return flow_id;
    }

    public void setFlow_id(int flow_id) {
        this.flow_id = flow_id;
    }

    public int getAction_id() {
        return action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }
}
