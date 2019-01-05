package com.pab.framework.crawlerengine.crawler;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.pab.framework.constant.Global;
import com.pab.framework.crawlerengine.dto.CrawlerJobInfo;
import com.pab.framework.crawlerengine.enums.UrlTypeEnum;
import com.pab.framework.crawlerengine.service.ProxyService;
import com.pab.framework.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
@Slf4j
public class CrawlerTurnpageHandlerImpl implements CrawlerHandler{

    private InheritableThreadLocal<Integer> urlType = new InheritableThreadLocal();
    private InheritableThreadLocal<String> xpath = new InheritableThreadLocal();
    private InheritableThreadLocal<List<Cookie>> cookies = new InheritableThreadLocal();

    @Autowired
    ProxyService proxyService;

    @Override
    public List<String> handler(CrawlerJobInfo crawlerJobInfo) throws Exception{
        this.xpath.set(crawlerJobInfo.getRegex());
        this.urlType.set(crawlerJobInfo.getUrlType());
        Spider spider = Spider.create(this).thread(4);
//        List<Proxy> proxyList = getProxyList();
//        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//        if(proxyList!=null && proxyList.size() > 0) {
//            httpClientDownloader.setProxyProvider(new SimpleProxyProvider(proxyList));
//            spider.setDownloader(httpClientDownloader);
//        }
        List<String> targetUrlList = new ArrayList();
        for(String url : crawlerJobInfo.getUrls()){
            ResultItems resultItems = spider.get(url);
            if(resultItems==null||resultItems.get(CrawlerContentTags.DYNAMIC_URLS) == null){
                log.error("没有可爬取的内容,url:"+url+" regex:"+crawlerJobInfo.getRegex());
            }else{
                targetUrlList.addAll((List<String>)resultItems.get(CrawlerContentTags.DYNAMIC_URLS));
            }
            TimeUnit.SECONDS.sleep(1);
        }
        return targetUrlList;
    }

    @Override
    public void process(Page page) {
        try {
            List<String> nodeList = new ArrayList<String>();
            if (urlType.get() == UrlTypeEnum.HTML.getLabel()) {
                nodeList = page.getHtml().xpath(xpath.get()).all();
            } else if (urlType.get() == UrlTypeEnum.JSON.getLabel()) {
                String xpathStr = xpath.get().split(Global.CRAWLER_REGEX_SPLIT)[0];
                String jsonpathStr = xpath.get().split(Global.CRAWLER_REGEX_SPLIT)[1];
                DocumentContext dc = JsonPath.parse(page.getHtml().xpath(xpathStr).toString());
                nodeList = dc.read(jsonpathStr);
            }
            page.putField(CrawlerContentTags.DYNAMIC_URLS, nodeList);
        }catch(Exception e){
            log.error("处理爬取内容失败:"+page.getUrl(),e);
        }
    }

    @Override
    public Site getSite() {
        Site site = Site.me().setRetryTimes(10)
                .setSleepTime(1000)
                .setRetrySleepTime(1000)
                .setTimeOut(5000)
                .setUseGzip(true)
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        for(Cookie cookie : cookies.get()){
            site.addCookie(cookie.getName(), cookie.getValue());
        }
        return site;
    }


    public static void main(String[] args) throws Exception {
        CrawlerTurnpageHandlerImpl chi = new CrawlerTurnpageHandlerImpl();
        CrawlerJobInfo cji = new CrawlerJobInfo();
        List<String> urls = new ArrayList();
        urls.add("https://xueqiu.com/v4/statuses/user_timeline.json?user_id=7558914709&page=1");
        cji.setUrls(urls);
        cji.setUrlType(2);
        cji.setRegex("//body/text()||$..target");
        //chi.handler("http://www.nifa.org.cn/nifa/2955686/2955720/e9b7b132/index3.html","//td[@id='list']//a[@target='_blank']/@href");
        //chi.handler("https://www.wdzj.com/", 1,"//div[@class='tabclist on']/ul[@class='newslist']//a[2]/@href");
        //chi.handler("http://www.iresearch.com.cn/products/GetReportList?classId=70&fee=0&date=&lastId=", 2,"//body||$..VisitUrl");
        //doHttpGet("https://xueqiu.com/v4/statuses/user_timeline.json?user_id=7558914709&page=1");
        //getCookies("https://xueqiu.com");
        //chi.handler("https://www.huxiu.com/member/1453857/article/1.html", 1,"//div[@id='per_center'//div[@class='mob-ctt']/h3/a/@href");
        chi.handler(cji);
    }

}
