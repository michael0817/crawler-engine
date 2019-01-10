package com.pab.framework.crawlerengine.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.List;

@Slf4j
public class HttpUtil {

    public static List<Cookie> getCookies(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            CookieStore cookieStore = new BasicCookieStore();
            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            HttpGet httpGet = new HttpGet(url);

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpGet.setConfig(requestConfig);

            response = httpClient.execute(httpGet);
            List<Cookie> cookies;
            if (HttpConstant.StatusCode.CODE_200 == response.getStatusLine().getStatusCode()) {
                cookies = cookieStore.getCookies();
                return cookies;
            }
            log.error("cookie获取失败:" + response.getStatusLine());
            return null;
        } catch (Exception e) {
            log.error("Http协议出现问题", e);
            return null;
        } finally {
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (Exception e) {
                    log.error("释放连接出错", e);
                }
            }
        }
    }

    public static void doHttpGet(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler()).build();
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpGet.setConfig(requestConfig);

            response = httpClient.execute(httpGet);
            if (HttpConstant.StatusCode.CODE_200 == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                String entityStr = EntityUtils.toString(entity, "UTF-8");
                log.info(entityStr);
            } else {
                log.error("get请求失败:" + response.getStatusLine());
            }
            return;
        } catch (Exception e) {
            log.error("Http协议出现问题", e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (Exception e) {
                    log.error("释放连接出错", e);
                }
            }
        }
    }

    public static void doHttpPost(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler()).build();
            HttpPost httpPost = new HttpPost(url);


            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpPost.setConfig(requestConfig);

            response = httpClient.execute(httpPost);
            if (HttpConstant.StatusCode.CODE_200 == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                String entityStr = EntityUtils.toString(entity, "UTF-8");
                log.info(entityStr);
            } else {
                log.error("get请求失败:" + response.getStatusLine());
            }
            return;
        } catch (Exception e) {
            log.error("Http协议出现问题", e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (Exception e) {
                    log.error("释放连接出错", e);
                }
            }
        }
    }

    public static void main(String[] args) {
        getCookies("https://xueqiu.com");
    }
}