package com.pab.framework.crawlerengine.vo;

import lombok.Data;

import java.util.List;

@Data
public class CrawlerJobInfo {
    private List<String> urls;
    private int actionType;
    private int urlType;
    private String regex;
    private List<org.apache.http.cookie.Cookie> cookies;
}