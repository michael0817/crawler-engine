package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class CrawlerLog extends BaseEntity {
    private int flowId;
    private int actionId;
    private Date operTime;
    private String resultCode;
    private String resultMessage;
}