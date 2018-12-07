package com.mall.cmbchina.product.post.html;

import com.mall.cmbchina.http.HttpPostRequest;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
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
public final class Consult {

    private static final Map<String, String> map = new HashMap<>();


    public  static StringBuilder getConsultList(String productCode) throws IOException, InterruptedException {

        List<NameValuePair> list = new LinkedList<>();
        list.add(new BasicNameValuePair("productCode", productCode));
        HttpEntity entity= HttpPostRequest.getEntity(list,"https://ssl.mall.cmbchina.com/_CL5_/Product/GetConsultList");
        Document document = Jsoup.parse(EntityUtils.toString(entity, "UTF-8"));
        Elements elements = document.getElementsByTag("dl");
        Elements  recordEles=document.getElementsByTag("em");
        int size=recordEles.size();
        if (size>0){
            Element recordEle=recordEles.get(0);
            String record =recordEle.text();
            int pageSize =elements.size();
            map.put("pageSize", String.valueOf(pageSize));
            map.put("record", record);
            StringBuilder builder = new StringBuilder();
            builder.append(recordEle.outerHtml());
            for (int i = 0; i < pageSize; i++) {
                int num = Integer.parseInt(record);
                if (num > 0) {
                    builder.append(elements.get(i).outerHtml());
                    builder.append("\n");
                }
            }
            return builder;
        }
        return null;

    }

    public  static StringBuilder getConsultList(String productCode, String record, String pageSize) throws IOException, InterruptedException {
        int maxPage = 0;
        int page = Integer.parseInt(pageSize);
        int num = Integer.parseInt(record);
        if (num > 0 && page > 0) {
            maxPage = (num - 1) / page + 1;
        }
        List<NameValuePair> list = new LinkedList<>();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        Document document = null;
        Elements elements = null;
        StringBuilder builder = null;
        for (AtomicInteger atomicInteger = new AtomicInteger(2); atomicInteger.get() < maxPage; atomicInteger.getAndIncrement()) {
            list.add(new BasicNameValuePair("productCode", productCode));
            list.add(new BasicNameValuePair("pageIndex", atomicInteger.get() + ""));
            list.add(new BasicNameValuePair("pageSize", pageSize));

            entity=HttpPostRequest.getEntity(list,"https://ssl.mall.cmbchina.com/_CL5_/Product/GetConsultList");
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

    public static StringBuilder getConsultList(String productCode, String pageSize) throws IOException, InterruptedException {
        List<NameValuePair> list = new LinkedList<>();
        list.add(new BasicNameValuePair("productCode", productCode));
        list.add(new BasicNameValuePair("pageSize", pageSize));

        HttpEntity entity=HttpPostRequest.getEntity(list,"https://ssl.mall.cmbchina.com/_CL5_/Product/GetConsultList");
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

    public  static StringBuilder htmlBuilder(String productCode) throws IOException, InterruptedException {
        StringBuilder builder = getConsultList(productCode);
        String pageSize = map.get("pageSize");
        String record = map.get("record");
        if (pageSize!=null&&record!=null){
            if (Integer.parseInt(pageSize) > 0 && Integer.parseInt(record) > 0) {
                builder.append(getConsultList(productCode,record,pageSize ));
            }
            if (Integer.parseInt(pageSize) > 0) {
                builder.append(getConsultList(productCode,pageSize ));
            }
        }

        return builder;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        //401345
//S1H-50T-2PF-06_015
        System.out.println(Consult.htmlBuilder("S1H-50T-2PF-06_015"));
    }

}
