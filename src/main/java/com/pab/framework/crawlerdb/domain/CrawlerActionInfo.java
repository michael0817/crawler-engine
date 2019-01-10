package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;
import lombok.Data;

@Data
public class CrawlerActionInfo extends BaseEntity {
    private int actionId;
    private String actionDesc;
    private int actionType;
    private int urlType;
    private String urlAddr;
    private String baseUrlAddr;
    private String crawlerRegex;
}