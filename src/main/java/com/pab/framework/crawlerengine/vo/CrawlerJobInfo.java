package com.pab.framework.crawlerengine.vo;

import lombok.Data;

import java.util.List;

@Data
/**
 * xumx
 * 爬取动作执行入参
 */
public class CrawlerJobInfo {
    //待爬取的url列表
    private List<String> urls;
    //爬取动作类型
    private int actionType;
    //url类型
    private int urlType;
    //正则（xpath、regex）
    private String regex;
    //cookies
    private List<org.apache.http.cookie.Cookie> cookies;
}