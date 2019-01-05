package com.pab.framework.crawlerdb.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;
import lombok.Data;

/**
 * Created by fando on 2018/11/5 17:17
 */
@Data
public class CrawlerFlowInfo extends BaseEntity {

    private Integer flowId;
    private String flowDesc;
    private Integer flowType;
    private String flowSchedule;

    @Override
    public String toString() {
        return "CrawlerFlowInfo{" +
                "flowId=" + flowId +
                ", flowDesc='" + flowDesc + '\'' +
                '}';
    }
}
