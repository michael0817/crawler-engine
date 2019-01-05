package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerNewsMilestone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrawlerNewsMilestoneDao {
   int save(CrawlerNewsMilestone crawlerNewsMilestone);
   List<String> findUrlAddrsByNewDate(Integer actionId);
   int  isExistsUrl(CrawlerNewsMilestone crawlerNewsMilestone);

}