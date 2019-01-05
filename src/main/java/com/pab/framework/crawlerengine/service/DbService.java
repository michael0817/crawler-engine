package com.pab.framework.crawlerengine.service;

import com.pab.framework.crawlerdb.dao.CrawlerActionDynamicInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerFlowDetailDao;
import com.pab.framework.crawlerdb.domain.CrawlerActionDynamicInfo;
import com.pab.framework.crawlerdb.domain.CrawlerFlowDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author xumx
 * @date 2019/1/3
 */
@Slf4j
@Service
public class DbService {

    @Autowired
    private CrawlerActionDynamicInfoDao crawlerActionDynamicInfoDao;
    @Autowired
    private CrawlerFlowDetailDao crawlerFlowDetailDao;
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;

    public void saveDynamicUrls(List<String> content, Integer actionId){
        Date date = new Date();
        for(String item : content) {
            CrawlerActionDynamicInfo cadi = new CrawlerActionDynamicInfo();
            cadi.setActionId(actionId);
            cadi.setContent(item);
            cadi.setOperTime(date);
            crawlerActionDynamicInfoDao.save(cadi);
        }
    }

    public void deleteDynamicUrls(Integer actionId){
        crawlerActionDynamicInfoDao.deleteAll(actionId);
    }


    @Transactional
    public void updateDynamicUrls(List<String> content, Integer actionId){
        deleteDynamicUrls(actionId);
        saveDynamicUrls(content,actionId);
    }


    public Integer getNextActionId(Integer actionId){
        try {
            List<CrawlerFlowDetail> cfds = crawlerFlowDetailDao.findNextAction(actionId);
            if (cfds != null && cfds.size() > 0) {
                return cfds.get(0).getActionId();
            }
        }catch(Exception e){
            log.error("SQL执行异常",e);
        }
        return -1;
    }

    public Integer getFlowIdByActionId(Integer actionId){
        try {
            return crawlerActionInfoDao.findFlowId(actionId);
        }catch(Exception e){
            log.error("SQL执行异常",e);
        }
        return -1;

    }
}
