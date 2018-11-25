package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawActionDynamicInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * Created by fando on 2018/11/4 17:04
 */
@Mapper
public interface CrawActionDynamicInfoDao {
    void deleteAll(int actionId);
    int insertAll(CrawActionDynamicInfo crawActionDynamicInfo);
    List<String> findAllUrlAddr(int actionId);

}
