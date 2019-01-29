package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class CrawlerActionDynamicInfo extends BaseEntity {
    private int actionId;
    private String articleId;
    private String articleName;
    private String articleContent;
    private int orderNum;
    private Date operTime;
}