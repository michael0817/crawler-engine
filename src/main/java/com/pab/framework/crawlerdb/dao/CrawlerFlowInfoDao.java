package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerFlowInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrawlerFlowInfoDao {

    List<CrawlerFlowInfo> findAll();

    CrawlerFlowInfo findOne(int paramInt);
}