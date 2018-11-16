package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;

import java.util.List;

/**
 * Created by fando on 2018/11/5 17:27
 */

public interface CrawlerFlowDetailDao {
    List<CrawlerFlowDetail> findAll();
    List<CrawlerFlowDetail> findAllByFlowId(int flow_id);
}
