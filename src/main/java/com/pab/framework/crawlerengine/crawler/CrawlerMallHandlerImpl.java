package com.pab.framework.crawlerengine.crawler;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.cookie.Cookie;
import org.springframework.stereotype.Component;

import com.itextpdf.text.log.SysoCounter;
import com.pab.framework.crawlercore.constant.Global;
import com.pab.framework.crawlerengine.enums.UrlTypeEnum;
import com.pab.framework.crawlerengine.model.CrawlerJobInfo;
import com.pab.framework.crawlerengine.model.News;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;


@Component
@Slf4j
public  class CrawlerMallHandlerImpl implements CrawlerHandler{

	private InheritableThreadLocal<Integer> urlType = new InheritableThreadLocal();
    private InheritableThreadLocal<String> regex = new InheritableThreadLocal();
    private InheritableThreadLocal<List<Cookie>> cookies = new InheritableThreadLocal();
	
    @Override
	public List<String> handler(CrawlerJobInfo crawlerJobInfo) throws Exception {
    	List<String> htmlList = new ArrayList();
    	this.regex.set(crawlerJobInfo.getRegex());
    	this.urlType.set(crawlerJobInfo.getUrlType());
        Spider spider = Spider.create(this).thread(4);
        List<ResultItems> resultItemsList = spider.getAll(crawlerJobInfo.getGetUrls());
        if ((resultItemsList == null) || (resultItemsList.size() == 0)) {
            log.error("没有可爬取的内容,regex:" + crawlerJobInfo.getRegex());
        } else {
            for (ResultItems ri : resultItemsList) {
            	htmlList.add(ri.get(Global.CRAWLER_HTML));
            }
            TimeUnit.SECONDS.sleep(1L);
        }
        return htmlList;
	}
	
    @Override
	public void process(Page page) {
    	try {
    		page.putField(Global.CRAWLER_HTML, page.getHtml().xpath(this.regex.get()).get());
    	}catch(Exception e) {
    		log.error("处理爬取内容失败:" + page.getUrl(), e);
    	}
	}
	
    
    @Override
    public Site getSite() {
        Site site = Site.me()
        		.setRetryTimes(10)
        		.setSleepTime(1000)
        		.setRetrySleepTime(1000)
        		.setTimeOut(5000)
        		.setUseGzip(true)
        		.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        if (this.cookies.get() != null) {
            for (Cookie cookie : this.cookies.get()) {
                site.addCookie(cookie.getName(), cookie.getValue());
            }
        }
        return site;
    }
    
    public static void main(String[] args) throws Exception {
        CrawlerMallHandlerImpl chi = new CrawlerMallHandlerImpl();
        CrawlerJobInfo cji = new CrawlerJobInfo();
        List<String> urls = new ArrayList();
        //urls.add("https://ssl.mall.cmbchina.com/_CL5_/Product/Detail?productCode=S1H-40R-2GO-07_099&pushwebview=1&productIndex=1");
        urls.add("https://ssl.mall.cmbchina.com/_CL5_/Product/ProductListAjaxLoad?subCategory=370&navigationKey=&sort=70&pageIndex=1");
        cji.setGetUrls(urls);
        cji.setRegex("/html");
        cji.setUrlType(Integer.valueOf(UrlTypeEnum.HTML.getLabel()));
        chi.handler(cji);
        List<String> htmlList = chi.handler(cji);
        System.out.println(htmlList.get(0));
    } 
	
}
