package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class CrawlerMilestone extends BaseEntity {
    private int actionId;
    private Date crawlerDate;
    private String milestone;
}