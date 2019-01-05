package com.pab.framework.crawlerengine.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.proxy.Proxy;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xumx
 * @date 2018/12/29
 */
@Service
@Slf4j
public class ProxyService {

//    proxy:
//    authUrl: http://120.52.79.8/haipAuth/
//    proxyUrl: http://120.52.79.8/haipBus/haip/fetchOne
//    id: pabccrawler
//    token: IkGd1jh=
    @Value("${proxy.authUrl}")
    private String authUrl;
    @Value("${proxy.proxyUrl}")
    private String proxyUrl;
    @Value("${proxy.id}")
    private String proxyId;
    @Value("${proxy.token}")
    private String proxyToken;

    private String tokenStr = "";
    private LocalDate tokenDate = null;
    private Set<String> proxyIpSet = new HashSet<String>(5);

    @Autowired
    ThreadService threadService;

    public synchronized String getNewToken(){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try{
            httpClient = HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler()).build();
            HttpPost httpPost = new HttpPost(authUrl);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            // 创建请求参数
            List<NameValuePair> list = new LinkedList<>();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("id", proxyId);
            jsonParam.put("code", proxyToken);
            StringEntity paramEntity = new StringEntity(jsonParam.toString(),"utf-8");//解决中文乱码问题
            paramEntity.setContentEncoding("UTF-8");
            paramEntity.setContentType("application/json");
            httpPost.setEntity(paramEntity);
            // 执行请求
            response = httpClient.execute(httpPost);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){
                // 获得响应的实体对象
                HttpEntity entity = response.getEntity();
                String entityStr = EntityUtils.toString(entity, "UTF-8");
                this.tokenStr = entityStr;
                this.tokenDate = LocalDate.now();
                log.info("获得"+this.tokenDate+"代理Token:"+this.tokenStr);
                return entityStr;
            }
            return null;
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

    public String getToken(){
        if(LocalDate.now()==this.tokenDate){
            return this.tokenStr;
        }
        synchronized (this) {
            if(LocalDate.now()==this.tokenDate) {
                return this.tokenStr;
            }
            return getNewToken();
        }
    }

    public Set<String> getProxyIpSet(){
        return this.proxyIpSet;
    }

    public void getNewProxyIp(){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler()).build();
            HttpGet httpGet = new HttpGet(proxyUrl + proxyId);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .build();//设置请求和传输超时时间
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("haip-token", getToken());
            // 执行请求
            response = httpClient.execute(httpGet);
            if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode()){
                // 获得响应的实体对象
                HttpEntity entity = response.getEntity();
                String entityStr = EntityUtils.toString(entity, "UTF-8");
                JSONObject jobj = JSONObject.parseObject(entityStr);
                String proxyIp = jobj.getJSONObject("rs").getString("id");
                if(StringUtils.isNotBlank(proxyIp)){
                    log.info("获得代理地址:"+proxyIp);
                    this.proxyIpSet.add(proxyIp);
                }
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


    @PostConstruct
    public void init(){
        threadService.getExecutorService().execute(() -> {
            while(true){
                if(proxyIpSet.size()<3){
                    getNewProxyIp();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        log.error("Sleep异常",e);
                    }
                }
            }
        });
    }

    private List<Proxy> getProxyList(){
        List<Proxy> proxyList = new ArrayList<Proxy>();
        try {
            Set<String> ipSet = this.getProxyIpSet();
            for (String ip : ipSet) {
                String[] ipArray = ip.split(":");
                if (ipArray.length == 2) {
                    Proxy proxy = new Proxy(ipArray[0], Integer.parseInt(ipArray[1]));
                    proxyList.add(proxy);
                }
            }
        }catch(Exception e){
            log.error("获取代理IP失败",e);
        }
        return proxyList;
    }

    public static void main(String[] args){
        System.out.println(LocalDate.now().toString());
    }
}
