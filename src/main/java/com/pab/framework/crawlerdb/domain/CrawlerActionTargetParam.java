package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;
import lombok.Data;

@Data
public class CrawlerActionTargetParam extends BaseEntity {
    private int actionId;
    private String paramType;
    private String paramName;
    private String paramValue;
}