package com.mall.cmbchina.product.post.json;

import com.alibaba.fastjson.JSONObject;
import com.mall.cmbchina.http.HttpPostRequest;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public final class Desc {
//S1H-M0I-2OQ_220
    public  static  String getDesc(String productCode) throws IOException, InterruptedException {
   return getDesc("https://ssl.mall.cmbchina.com/_CL5_/Product/GetDesc",productCode);
    }
//"https://ssl.mall.cmbchina.com/_CL5_/Product/GetDesc"
    public  static  String getDesc(String uri,String productCode) throws IOException, InterruptedException {
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> list = new LinkedList<>();
        list.add(new BasicNameValuePair("productCode", productCode));
        HttpEntity entity=HttpPostRequest.getEntity(list,uri);
        InputStream is = entity.getContent();
        InputStreamReader isr=new InputStreamReader(is);
        BufferedReader br=new BufferedReader(isr);
        String line;
        while((line=br.readLine())!=null){
            builder.append(line);
        }
        JSONObject jsonObject=JSONObject.parseObject(builder.toString());
        return jsonObject.getString("Results");
    }




    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(Desc.getDesc("S1H-M0I-2OQ_220"));
    }



}
