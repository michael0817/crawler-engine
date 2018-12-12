package com.pab.framework.crawlerengine.processor.action;

import com.pab.framework.crawlerdb.dao.CrawerActionDynamicInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerNewsMilestoneDao;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerengine.crawler.CrawlerHandler;
import com.pab.framework.crawlerengine.processor.DetailPageProcessor;
import com.pab.framework.crawlerengine.processor.DetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.processor.LoginDetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.util.CrawlerUtil;
import com.pab.framework.crawlerengine.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
public class NewsActionProcessorImpl implements ActionProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DetailUrlsPageProcessor detailUrlsPageProcessor;
    @Autowired
    private DetailPageProcessor detailPageProcessor;
    @Autowired
    private LoginDetailUrlsPageProcessor loginDetailUrlsPageProcessor;
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;
    @Autowired
    private CrawerActionDynamicInfoDao crawerActionDynamicInfoDao;
    @Autowired
    private CrawlerNewsMilestoneDao crawlerNewsMilestoneDao;
    @Autowired
    private CrawlerHandler crawlerHandler;


    @Override
    public List<String> getUrls(List<CrawlerActionInfo> crawlerActionInfos, Map<Integer,List<String>> urlAddrs) throws IOException {

        List<String> addrs=new LinkedList<>();
        CrawlerActionInfo info;
        for (CrawlerActionInfo crawlerActionInfo : crawlerActionInfos) {
            Integer actionType = crawlerActionInfo.getActionType();
            if (3==actionType){
                String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();
                String urlAddr = crawlerActionInfo.getUrlAddr();
                int actionId = CrawlerUtil.extractIntOfStr(urlAddr, "[", "]");
                info= crawlerActionInfoDao.findCrawlerActionInfo(actionId);
                String crawlerRegex = info.getCrawlerRegex();
                List<String> process = detailUrlsPageProcessor.process(crawlerRegex, urlAddrs.get(actionId));
                addrs.addAll(process);
                crawlerHandler.handler(info,addrs);
            }
        }


        return addrs;
    }

    @Override
    public  Map<Integer,List<String>> getUrlAddrs(List<CrawlerActionInfo> crawlerActionInfos) {
        List<String>    urlAddrs;
        Map<Integer,List<String>>             map=new HashMap<>();
        for (CrawlerActionInfo crawlerActionInfo : crawlerActionInfos) {
            Integer actionType = crawlerActionInfo.getActionType();
            if (2==actionType){

                urlAddrs= new LinkedList<>();
                String urlAddr = crawlerActionInfo.getUrlAddr();
                String prefix = urlAddr.substring(0, urlAddr.indexOf("{"));
                String suffix = urlAddr.substring(urlAddr.indexOf("}") + 1);
                int index = UrlUtils.maxPage(urlAddr);
                String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();
                Integer actionId = crawlerActionInfo.getActionId();
                for (int i = 1; i <= index; i++) {

                    //雪球网零时处理
                    if ("https://xueqiu.com".equals(baseUrlAddr)) {
                        prefix = "/v4/statuses/user_timeline.json?page=";
                        suffix = "&user_id=7558914709";
                    }
                    urlAddrs.add(baseUrlAddr + prefix + i + suffix);
                    map.put(actionId,urlAddrs);
                }
            }
        }

        return map;
    }

    @Override
    public void process(List<Integer> actionIds) throws IOException {
        List<CrawlerActionInfo> crawlerActionInfos = crawlerActionInfoDao.findCrawlerActionInfos(actionIds);
        Map<Integer,List<String>> urlAddrs = getUrlAddrs(crawlerActionInfos);
        getUrls(crawlerActionInfos, urlAddrs);
    }
}