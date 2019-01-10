package com.pab.framework.crawlerengine.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ProxyService {

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
    private Set<String> proxyIpSet = new java.util.HashSet(5);

    @Autowired
    ThreadService threadService;

    public synchronized String getNewToken() {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler()).build();
            HttpPost httpPost = new HttpPost(this.authUrl);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpPost.setConfig(requestConfig);

            List<NameValuePair> list = new java.util.LinkedList();
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("id", this.proxyId);
            jsonParam.put("code", this.proxyToken);
            StringEntity paramEntity = new StringEntity(jsonParam.toString(), "utf-8");
            paramEntity.setContentEncoding("UTF-8");
            paramEntity.setContentType("application/json");
            httpPost.setEntity(paramEntity);

            response = httpClient.execute(httpPost);
            HttpEntity entity;
            if (200 == response.getStatusLine().getStatusCode()) {
                entity = response.getEntity();
                String entityStr = EntityUtils.toString(entity, "UTF-8");
                this.tokenStr = entityStr;
                this.tokenDate = LocalDate.now();
                log.info("获得" + this.tokenDate + "代理Token:" + this.tokenStr);
                return entityStr;
            }
            return null;
        } catch (Exception e) {
            RequestConfig requestConfig;
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

    public String getToken() {
        if (LocalDate.now() == this.tokenDate) {
            return this.tokenStr;
        }
        synchronized (this) {
            if (LocalDate.now() == this.tokenDate) {
                return this.tokenStr;
            }
            return getNewToken();
        }
    }

    public Set<String> getProxyIpSet() {
        return this.proxyIpSet;
    }

    public void getNewProxyIp() {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.custom().setRetryHandler(new StandardHttpRequestRetryHandler()).build();
            HttpGet httpGet = new HttpGet(this.proxyUrl + this.proxyId);


            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("haip-token", getToken());

            response = httpClient.execute(httpGet);
            if (200 == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                String entityStr = EntityUtils.toString(entity, "UTF-8");
                JSONObject jobj = JSONObject.parseObject(entityStr);
                String proxyIp = jobj.getJSONObject("rs").getString("id");
                if (StringUtils.isNotBlank(proxyIp)) {
                    log.info("获得代理地址:" + proxyIp);
                    this.proxyIpSet.add(proxyIp);
                }
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

    @PostConstruct
    public void init() {
    }

    private List<Proxy> getProxyList() {
        List<Proxy> proxyList = new ArrayList();
        try {
            Set<String> ipSet = getProxyIpSet();
            for (String ip : ipSet) {
                String[] ipArray = ip.split(":");
                if (ipArray.length == 2) {
                    Proxy proxy = new Proxy(ipArray[0], Integer.parseInt(ipArray[1]));
                    proxyList.add(proxy);
                }
            }
        } catch (Exception e) {
            log.error("获取代理IP失败", e);
        }
        return proxyList;
    }

    public static void main(String[] args) {
        System.out.println(LocalDate.now().toString());
    }
}