package com.pab.framework.crawlerdb.domain;

/**
 * Created by fando on 2018/11/3 13:23
 */
public class CrawlerActionInfo {
    private int action_id;
    private String action_desc;
    private int action_type;
    private int url_type;
    private String url_addr;
    private String base_url_addr;
    private String crawler_regex;




    public int getAction_id() {
        return action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
    }

    public String getAction_desc() {
        return action_desc;
    }

    public void setAction_desc(String action_desc) {
        this.action_desc = action_desc;
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

    public String getBase_url_addr() {
        return base_url_addr;
    }

    public void setBase_url_addr(String base_url_addr) {
        this.base_url_addr = base_url_addr;
    }

    public String getCrawler_regex() {
        return crawler_regex;
    }

    public void setCrawler_regex(String crawler_regex) {
        this.crawler_regex = crawler_regex;
    }
}
