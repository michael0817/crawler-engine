package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrawlerFlowDetailDao {

    List<CrawlerFlowDetail> findAll();

    List<CrawlerFlowDetail> findAllByFlowId(int paramInt);

    List<CrawlerFlowDetail> findNextAction(int paramInt);

    int findFlowIdByActionId(int paramInt);
}