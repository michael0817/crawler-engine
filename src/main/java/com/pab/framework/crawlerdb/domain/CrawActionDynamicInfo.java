package com.pab.framework.crawlerdb.domain;

/**
 * Created by fando on 2018/11/4 17:00
 */
public class CrawActionDynamicInfo {

    private  int dyn_action_id;
    private  int action_id;
    private int action_type;
    private  int url_type;
    private String url_addr;

    public int getDyn_action_id() {
        return dyn_action_id;
    }

    public void setDyn_action_id(int dyn_action_id) {
        this.dyn_action_id = dyn_action_id;
    }

    public int getAction_id() {
        return action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
    }

    public int getAction_type() {
        return action_type;
    }

    public void setAction_type(int action_type) {
        this.action_type = action_type;
    }

    public int getUrl_type() {
        return url_type;
    }

    public void setUrl_type(int url_type) {
        this.url_type = url_type;
    }

    public String getUrl_addr() {
        return url_addr;
    }

    public void setUrl_addr(String url_addr) {
        this.url_addr = url_addr;
    }
}
