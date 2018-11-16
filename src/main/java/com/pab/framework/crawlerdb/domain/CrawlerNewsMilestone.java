package com.pab.framework.crawlerdb.domain;

import java.util.Date;

/**
 * Created by fando on 2018/11/4 18:00
 */
public class CrawlerNewsMilestone {
    private  int action_id;
    private String url_addr;
    private Date crawler_date;

    public CrawlerNewsMilestone() {
    }

    public CrawlerNewsMilestone(int action_id, String url_addr) {
        this.action_id = action_id;
        this.url_addr = url_addr;
    }

    public int getAction_id() {
        return action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
    }

    public String getUrl_addr() {
        return url_addr;
    }

    public void setUrl_addr(String url_addr) {
        this.url_addr = url_addr;
    }

    public Date getCrawler_date() {
        return crawler_date;
    }

    public void setCrawler_date(Date crawler_date) {
        this.crawler_date = crawler_date;
    }
}
