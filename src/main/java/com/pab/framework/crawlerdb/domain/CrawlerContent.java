package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CrawlerContent extends BaseEntity {
    private int actionId;
    private int actionType;
    private LocalDate crawlerDate;
    private String content;
}