package com.pab.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author xumx
 * @date 2018/12/29
 */
@Slf4j
public class HttpUtil {

    public static List<Cookie> getCookies(String url){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            CookieStore cookieStore = new BasicCookieStore();
            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .build();//设置请求和传输超时时间
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){
                List<Cookie> cookies = cookieStore.getCookies();
                return cookies;
            }else{
                log.error("cookie获取失败:"+response.getStatusLine());
                return null;
            }
        } catch (Exception e) {
            log.error("Http协议出现问题",e);
            return null;
        } finally {
            // 释放连接
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (Exception e) {
                    log.error("释放连接出错",e);
                }
            }
        }
    }

    public static void doHttpGet(String url){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler()).build();
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .build();//设置请求和传输超时时间
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){
                // 获得响应的实体对象
                HttpEntity entity = response.getEntity();
                String entityStr = EntityUtils.toString(entity, "UTF-8");
                log.info(entityStr);
            }else{
                log.error("get请求失败:"+response.getStatusLine());
            }
        } catch (Exception e) {
            log.error("Http协议出现问题",e);
        } finally {
            // 释放连接
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (Exception e) {
                    log.error("释放连接出错",e);
                }
            }
        }
    }

    public static void doHttpPost(String url){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler()).build();
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){
                // 获得响应的实体对象
                HttpEntity entity = response.getEntity();
                String entityStr = EntityUtils.toString(entity, "UTF-8");
                log.info(entityStr);
            }else{
                log.error("get请求失败:"+response.getStatusLine());
            }
        } catch (Exception e) {
            log.error("Http协议出现问题",e);
        } finally {
            // 释放连接
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (Exception e) {
                    log.error("释放连接出错",e);
                }
            }
        }
    }

    public static void main(String[] args){
        getCookies("https://xueqiu.com");
    }
}
