package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;
import lombok.Data;

@Data
public class CrawlerFlowDetail extends BaseEntity {
    private int flowId;
    private int actionId;
    private int orderNum;
}