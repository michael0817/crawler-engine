package com.pab.framework.crawlerengine.processor.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.cookie.Cookie;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pab.framework.crawlercore.constant.Global;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerLog;
import com.pab.framework.crawlerdb.service.DbService;
import com.pab.framework.crawlerengine.cache.ICache;
import com.pab.framework.crawlerengine.crawler.CrawlerHandler;
import com.pab.framework.crawlerengine.model.CrawlerJobInfo;
import com.pab.framework.crawlerengine.util.CrawlerUtil;

import lombok.extern.slf4j.Slf4j;

/*
 * xn
 */
@Component
@Slf4j
public class MallActionProcessor  implements ActionProcessor{

	@Autowired
	private CrawlerHandler crawlerMallHandlerImpl;
	@Autowired
	private DbService dbService;
	@Autowired
	private ICache localcache;
	
	@Override
	public boolean actionHandler(CrawlerActionInfo cai) {
		
		try {
			int preActionId = CrawlerUtil.getActionId(cai.getUrlAddr());
			CrawlerJobInfo cji = new CrawlerJobInfo();
			//url
			List<String>  urlList= this.dbService.getDynamicContentsByActionId(preActionId);
			if(urlList.size() == 0) {
				CrawlerLog cl = new CrawlerLog();
				cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
				cl.setActionId(cai.getActionId());
				cl.setOperTime(new Date());
				cl.setResultCode(Global.CRAWLER_RESULT_SUCCESS);
				cl.setResultMessage("无可爬取商品");
				this.dbService.saveLog(cl);
				return true;
			}
			
			 for(int i = 0;i<urlList.size();i++) {
			       if(StringUtils.isNotBlank(urlList.get(i))) {
			    	   //截取url
			    	   String tempUrl = urlList.get(i);
			    	   String url = tempUrl.substring(tempUrl.indexOf(Global.HTTPS_FLAG), tempUrl.indexOf(Global.MALL_URL_SPLIT));
			    	   urlList.set(i, url);
			       }
			 }
			cji.setGetUrls(urlList);
			cji.setRegex(cai.getCrawlerRegex());
			cji.setActionType(cai.getActionType());
			cji.setUrlType(cai.getUrlType());
			cji.setCookies((List<Cookie>)(this.localcache.getCache(this.dbService.getFlowIdByActionId(cai.getActionId())
                    +"")));
			List<String> mallList = (List)this.crawlerMallHandlerImpl.handler(cji);
			if((mallList != null) && (mallList.size()>0)) {
				this.dbService.updateCrawlerMall(mallList, cai.getActionId());
			}
			//日志
			CrawlerLog cl = new CrawlerLog();
			if(urlList.size() == mallList.size()) {
				cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
				cl.setActionId(cai.getActionId());
				cl.setOperTime(new Date());
				cl.setResultCode(Global.CRAWLER_RESULT_SUCCESS);
				cl.setResultMessage("爬取商品总计" + mallList.size() + "条");
				log.info("爬取商品总计" + mallList.size() + "条");
			}else {
				cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
				cl.setActionId(cai.getActionId());
				cl.setOperTime(new Date());
				cl.setResultCode(Global.CRAWLER_RESULT_WARN);
				cl.setResultMessage("爬取商品总计" + mallList.size() + "条,失败" + (urlList.size() - mallList.size()) + "条" );
				log.info("爬取商品总计" + mallList.size() + "条,失败" + (urlList.size() - mallList.size()) + "条" );
			}
			this.dbService.saveLog(cl);
			return true;
		}catch(Exception e) {
			log.error("商品爬取出错,actionId:" + cai.getActionId(),e);
			CrawlerLog cl = new CrawlerLog();
			cl.setFlowId(this.dbService.getFlowIdByActionId(cai.getActionId()));
			cl.setActionId(cai.getActionId());
			cl.setOperTime(new Date());
			cl.setResultCode(Global.CRAWLER_RESULT_FAILURE);
			cl.setResultMessage("爬取商品出错:" + e.getMessage());
			this.dbService.saveLog(cl);
			log.error("爬取商品出错:" + e.getMessage());
		}
		return false;
	}
	
	public static void  main(String[] args) throws UnsupportedEncodingException {
		
		String a = "cmblife://go?url=web&next=https%3a%2f%2fssl.mall.cmbchina.com%2f_CL5_%2fProduct%2fDetail%3fproductCode%3dS1H-W2Q-2I6_006%26pushwebview%3d1%26productIndex%3d16&title=&version=v2";
		String c = URLDecoder.decode(a,"UTF-8");
		String d = c.substring(c.indexOf("https"), c.indexOf("&title="));
		System.out.println(c);
		System.out.println(d);
		
	}
	
	
	
	
	
}
