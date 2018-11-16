package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawActionDynamicInfo;

import java.util.List;
/**
 * Created by fando on 2018/11/4 17:04
 */

public interface CrawActionDynamicInfoDao {
    void deleteAll(int action_id);
    int insertAll(CrawActionDynamicInfo crawActionDynamicInfo);
    List<String> findAllUrlAddr(int action_id);

}
