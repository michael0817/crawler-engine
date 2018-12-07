package com.mall.cmbchina.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

public final class HttpPostRequest {

    public static HttpEntity getEntity(List<NameValuePair> list,String uri) throws IOException, InterruptedException {
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(entityParam);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        Thread.sleep(3000);
        HttpEntity entity = response.getEntity();
        return entity;
    }

}
