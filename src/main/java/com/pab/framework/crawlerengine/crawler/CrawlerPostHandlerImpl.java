package com.pab.framework.crawlerengine.crawler;

import com.pab.framework.crawlercore.constant.Global;
import com.pab.framework.crawlerengine.service.ProxyService;
import com.pab.framework.crawlerengine.vo.CrawlerJobInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.*;

import java.util.*;


@Component
@Slf4j
public class CrawlerPostHandlerImpl implements CrawlerHandler {

    private InheritableThreadLocal<Integer> urlType = new InheritableThreadLocal();
    private InheritableThreadLocal<String> regex = new InheritableThreadLocal();
    private InheritableThreadLocal<List<Cookie>> cookies = new InheritableThreadLocal();

    @Autowired
    ProxyService proxyService;

    public List<String> handler(CrawlerJobInfo crawlerJobInfo)
            throws Exception {
//        List<String> htmlList = new ArrayList();
//        this.regex.set(crawlerJobInfo.getRegex());
//        this.urlType.set(crawlerJobInfo.getUrlType());
//        Spider spider = Spider.create(this).thread(4);
//        List<Request> requestList = new ArrayList();
//        for(Map<String,String> param : crawlerJobInfo.getParamList()){
//            Request request = new Request(crawlerJobInfo.getPostUrl());
//            request.setMethod(HttpConstant.Method.POST);
//            request.setRequestBody(HttpRequestBody.form(param, "UTF-8"));
//        }
//        spider.addRequest(requestList.toArray(new Request[requestList.size()]));
//        spider.run();
//        if ((resultItemsList == null) || (resultItemsList.size() == 0)) {
//            log.error("没有可爬取的内容,regex:" + crawlerJobInfo.getRegex());
//        } else {
//            for (ResultItems ri : resultItemsList) {
//                htmlList.addAll(ri.get(Global.CRAWLER_HTML));
//            }
//            TimeUnit.SECONDS.sleep(1L);
//        }
//        return htmlList;
        return null;
    }

    public void process(Page page) {
        try {
            page.putField(Global.CRAWLER_HTML, page.getHtml().xpath(this.regex.get()).all());
        } catch (Exception e) {
            log.error("处理爬取内容失败:" + page.getUrl(), e);
        }
    }


    public Site getSite() {
        Site site = Site.me()
                .setRetryTimes(10)
                .setSleepTime(1000)
                .setRetrySleepTime(1000)
                .setTimeOut(5000)
                .setUseGzip(true)
                .setUserAgent("Mozilla/5.0 (Linux; Android 6.0.1; DUK-AL20 Build/MXC89K; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/44.0.2403.119 Mobile Safari/537.36");

        if (this.cookies.get() != null) {
            for (Cookie cookie : this.cookies.get()) {
                site.addCookie(cookie.getName(), cookie.getValue());
            }
        }
        return site;
    }

    public static void main(String[] args) throws Exception {
//        CrawlerPostHandlerImpl chi = new CrawlerPostHandlerImpl();
//        CrawlerJobInfo cji = new CrawlerJobInfo();
//        List<String> urls = new ArrayList();
//        //urls.add("https://ssl.mall.cmbchina.com/_CL5_/Category/GetAllCategories");
//        urls.add("https://ssl.mall.cmbchina.com/_CL5_/Category/GetCategories?id=5");
//        cji.setUrls(urls);
//        cji.setUrlType(Integer.valueOf(UrlTypeEnum.HTML.getLabel()));
//        //cji.setRegex("//div[@id='divC1Panel']//li");
//        cji.setRegex("//dl[@class='catalog_list']//dd");
//        List<String> htmlList = chi.handler(cji);
//        System.out.println(htmlList.size());

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler()).build();
            HttpPost httpPost = new HttpPost("https://ssl.mall.cmbchina.com/_cl4_/Product/Detail?=");
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpPost.setConfig(requestConfig);

            /*
             * 添加请求参数
             */
            // 创建请求参数
            List<NameValuePair> list = new LinkedList<>();
            BasicNameValuePair param1 = new BasicNameValuePair("productCode", "S1H-50T-27F-06_015");
            BasicNameValuePair param2 = new BasicNameValuePair("pushwebview", "1");
            BasicNameValuePair param3 = new BasicNameValuePair("productIndex", "42#");
            list.add(param1);
            list.add(param2);
            list.add(param3);
            // 使用URL实体转换工具
            UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(entityParam);

            /*
             * 添加请求头信息
             */
            // 浏览器表示
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
            // 传输的类型
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

            // 执行请求
            response = httpClient.execute(httpPost);
            // 获得响应的实体对象
            HttpEntity entity = response.getEntity();
            // 使用Apache提供的工具类进行转换成字符串
            String entityStr = EntityUtils.toString(entity, "GBK");
            //sb.append(entityStr.toString());

            System.out.println(entityStr);


        } catch (Exception e) {
            System.err.println("Http协议出现问题");
            e.printStackTrace();
        } finally {
            // 释放连接
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (Exception e) {
                    System.err.println("释放连接出错");
                    e.printStackTrace();
                }
            }
        }

    }

}