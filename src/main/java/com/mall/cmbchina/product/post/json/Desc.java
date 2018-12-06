package com.pab.framework.crawlerengine.mall.cmbchina.post.json;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Desc {
//S1H-M0I-2OQ_220
    public  String getDesc(String productCode) throws IOException, InterruptedException {
        StringBuffer buffer = new StringBuffer();
        List<NameValuePair> list = new LinkedList<>();
        list.add(new BasicNameValuePair("productCode", productCode));
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        HttpPost httpPost = new HttpPost("https://ssl.mall.cmbchina.com/_CL5_/Product/GetDesc");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(entityParam);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        Thread.sleep(3000);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        InputStreamReader isr=new InputStreamReader(is);
        BufferedReader br=new BufferedReader(isr);
        String line;
        while((line=br.readLine())!=null){
         buffer.append(line);
        }
        JSONObject jsonObject=JSONObject.parseObject(buffer.toString());
        return jsonObject.getString("Results");
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        Desc desc=new Desc();
        //  desc.getDesc("S1H-M0I-2OQ_220");
    }
}
