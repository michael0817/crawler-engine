package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;
import lombok.Data;

@Data
public class CrawlerFlowInfo extends BaseEntity {
    private int flowId;
    private String flowDesc;
    private int flowType;
    private String flowSchedule;
}