package com.mall.cmbchina.product.post.html;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class Review {
    private Map<String, String> map = new HashMap<>();

    public StringBuilder getReviewList(String productCode) throws IOException, InterruptedException {
        StringBuilder builder = new StringBuilder();
        List<NameValuePair> list = new LinkedList<>();
        list.add(new BasicNameValuePair("productCode", productCode));
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        HttpPost httpPost = new HttpPost("https://ssl.mall.cmbchina.com/_CL5_/Product/GetReviewList");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(entityParam);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        Thread.sleep(3000);
        HttpEntity entity = response.getEntity();
        Document document = Jsoup.parse(EntityUtils.toString(entity, "UTF-8"));
        Elements elements = document.getElementsByTag("dl");
        Element element = document.getElementsByTag("em").get(0);
        String record = element.text();
        int pageSize = elements.size();
        map.put("pageSize", String.valueOf(pageSize));
        map.put("record", record);
        builder.append(element.outerHtml());
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            int num = Integer.parseInt(record);
            if (num > 0) {
                builder.append(elements.get(i).outerHtml());
                builder.append("\n");
            }

        }
        return builder;

    }

    public StringBuilder getReviewList(String productCode, String record, String pageSize) throws IOException, InterruptedException {
        int maxPage = 0;
        int page = Integer.parseInt(pageSize);
        int num = Integer.parseInt(record);
        if (num > 0 && page > 0) {
            maxPage = (num - 1) / page + 1;
        }
        List<NameValuePair> list = new LinkedList<>();
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        UrlEncodedFormEntity entityParam = null;
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        Document document = null;
        Elements elements = null;
        StringBuilder builder = null;
        for (AtomicInteger atomicInteger = new AtomicInteger(2); atomicInteger.get() < maxPage; atomicInteger.getAndIncrement()) {
            list.add(new BasicNameValuePair("productCode", productCode));
            list.add(new BasicNameValuePair("pageIndex", atomicInteger.get() + ""));
            list.add(new BasicNameValuePair("pageSize", pageSize));
            entityParam = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost = new HttpPost("https://ssl.mall.cmbchina.com/_CL5_/Product/GetConsultList");
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(entityParam);
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(httpPost);
            Thread.sleep(3000);
            entity = response.getEntity();
            document = Jsoup.parse(EntityUtils.toString(entity, "UTF-8"));
            elements = document.getElementsByTag("dl");
            int size = elements.size();
            builder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                builder.append(elements.get(i).outerHtml());
                builder.append("\n");
            }
        }
        return builder;
    }

    public StringBuilder getReviewList(String productCode, String pageSize) throws IOException, InterruptedException {
        List<NameValuePair> list = new LinkedList<>();
        list.add(new BasicNameValuePair("productCode", productCode));
        list.add(new BasicNameValuePair("pageSize", pageSize));
        UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
        HttpPost httpPost = new HttpPost("https://ssl.mall.cmbchina.com/_CL5_/Product/GetConsultList");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(entityParam);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        Thread.sleep(3000);
        HttpEntity entity = response.getEntity();
        Document document = Jsoup.parse(EntityUtils.toString(entity, "UTF-8"));
        Elements elements = document.getElementsByTag("dl");
        StringBuilder builder = new StringBuilder();
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            builder.append(elements.get(i).outerHtml());
            builder.append("\n");
        }
        return builder;
    }

    public StringBuilder htmlBuilder(String productCode) throws IOException, InterruptedException {
        StringBuilder builder = getReviewList(productCode);
        String pageSize = map.get("pageSize");
        String record = map.get("record");
        if (Integer.parseInt(pageSize) > 0 && Integer.parseInt(record) > 0) {
            builder.append(getReviewList(productCode,record,pageSize ));
        }
        if (Integer.parseInt(pageSize) > 0) {
            builder.append(getReviewList(productCode,pageSize ));
        }
        return builder;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Review review = new Review();
        System.out.println(review.getReviewList("S1H-50T-2PF-06_015"));
    }

}
