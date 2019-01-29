package com.pab.framework.crawlerengine.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
/**
 * xumx
 * 爬取动作执行入参
 */
public class CrawlerJobInfo {
    //待爬取的get url列表
    private List<String> getUrls;
    //待爬取的post url
    private String postUrl;
    //待爬取的url入参（post）
    private List<Map<String,String>> paramList;
    //爬取动作类型
    private int actionType;
    //url类型
    private int urlType;
    //正则（xpath、regex）
    private String regex;
    //cookies
    private List<org.apache.http.cookie.Cookie> cookies;
}