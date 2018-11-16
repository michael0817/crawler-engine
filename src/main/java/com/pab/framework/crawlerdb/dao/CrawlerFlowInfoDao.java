package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerFlowInfo;

import java.util.List;
/**
 * Created by fando on 2018/11/5 17:19
 */

public interface CrawlerFlowInfoDao {
    List<CrawlerFlowInfo> findAll();
    CrawlerFlowInfo findOne(int flow_id);
}
