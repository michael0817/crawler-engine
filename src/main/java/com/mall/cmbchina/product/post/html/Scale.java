package com.mall.cmbchina.product.post.html;

import com.mall.cmbchina.http.HttpPostRequest;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public final  class Scale {
    public static String getScale(String productCode) throws IOException, InterruptedException {
        List<NameValuePair> list = new LinkedList<>();
        list.add(new BasicNameValuePair("productCode", productCode));
        HttpEntity entity = HttpPostRequest.getEntity(list, "https://ssl.mall.cmbchina.com/_CL5_/Product/GetScale");
        Document document = Jsoup.parse(EntityUtils.toString(entity, "UTF-8"));
        return document.body().html();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(Scale.getScale("S1H-50T-2PF-06_015"));
    }
}
