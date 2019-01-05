package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;
import lombok.Data;

import java.beans.Transient;

/**
 * Created by fando on 2018/11/3 13:23
 */
@Data
public class CrawlerActionInfo  extends BaseEntity {
    private Integer actionId;
    private String actionDesc;
    private Integer actionType;
    private Integer urlType;
    private String urlAddr;
    private String baseUrlAddr;
    private String crawlerRegex;
}
