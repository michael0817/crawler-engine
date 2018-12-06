package com.post.html;

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
    private final Map<String, String> map = new HashMap<>();

    public StringBuffer getReviewList(String productCode) throws IOException, InterruptedException {
        StringBuffer buffer = new StringBuffer();
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
        Elements elements = document.getElementsByClass("qna");
        String record = document.getElementsByTag("em").get(0).text();
        int pageSize = elements.size();
        map.put("pageSize", String.valueOf(pageSize));
        map.put("record", record);
        return htmlBuffer(elements);
    }

    public StringBuffer getReviewList(String productCode, String record, String pageSize) throws IOException, InterruptedException {
        int maxPage = (Integer.parseInt(record) - 1) / Integer.parseInt(pageSize) + 1;
        List<NameValuePair> list = new LinkedList<>();
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        UrlEncodedFormEntity entityParam = null;
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        Document document = null;
        Elements elements = null;
        StringBuffer buffer = null;
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
            elements = document.getElementsByClass("qna");
            int size = elements.size();
            buffer = new StringBuffer();
            for (int i = 0; i < size; i++) {
                buffer.append(elements.get(i).outerHtml());
                buffer.append("\n");
            }
        }
        return buffer;
    }

    public StringBuffer getReviewList(String productCode, String pageSize) throws IOException, InterruptedException {
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
        Elements elements = document.getElementsByClass("qna");
        StringBuffer buffer = new StringBuffer();
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            buffer.append(elements.get(i).outerHtml());
            buffer.append("\n");
        }
        return buffer;
    }

    public StringBuffer htmlBuffer(Elements elements) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(elements.tagName("em").get(0).outerHtml());
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            buffer.append(elements.get(i).outerHtml());
            buffer.append("\n");
        }
        return buffer;
    }

    public StringBuffer htmlBuffer(String productCode) throws IOException, InterruptedException {
        StringBuffer buffer = getReviewList(productCode);
        String pageSize = map.get("pageSize");
        String record = map.get("record");
        if (!"0".equals(record)) {
            buffer.append(getReviewList(productCode, record, pageSize));
            buffer.append(getReviewList(productCode, pageSize));
        }
        return buffer;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //401345
        Review review = new Review();
        System.out.println(review.htmlBuffer("401345"));
    }

}
