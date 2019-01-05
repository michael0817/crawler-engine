package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by fando on 2018/11/4 17:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CrawlerActionDynamicInfo extends BaseEntity {

    private Integer dynActionId;
    private Integer actionId;
    private String  content;
    private Date operTime;
}
