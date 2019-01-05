package com.pab.framework.crawlerengine.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.example.BaiduBaikePageProcessor;
import us.codecraft.webmagic.processor.example.ZhihuPageProcessor;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public final class UrlUtils {

    private static final String pagePrefix = "{";
    private static final String pageSubfix = "}";
    private static final String pageSplit = "~";

    /**
     * 信任任何站点，实现https页面的正常访问
     */

    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static final void printUrl() throws IOException {
        trustEveryone();
        Connection connect = Jsoup.connect("http://www.iresearch.com.cn/report.shtml");
        Document document = connect.get();
        Elements elements = document.getElementsByTag("a");
        int size = elements.size();
        for (int i = 0; i < size; i++) {
            System.out.println(elements.get(i).getElementsByAttribute("href"));
        }
    }

//    public static int maxPage(String urlAddr) {
//        Pattern pattern = Pattern.compile("\\d+}");
//        Matcher matcher = pattern.matcher(urlAddr);
//        if (matcher.find()) {
//            String group = matcher.group();
//            return Integer.parseInt(group.substring(0, group.length() - 1));
//        }
//        return -1;
//    }

    public static int getStartPageIndex(String urlAddr){
        try {
            if(urlAddr.indexOf(pagePrefix)<0){
                return 0;
            }
            String start = urlAddr.substring(urlAddr.indexOf(pagePrefix) + 1, urlAddr.indexOf(pageSplit));
            return Integer.parseInt(start);
        }catch(Exception e){
            log.error("页码解析出错", e);
            return -1;
        }
    }

    public static int getEndPageIndex(String urlAddr){
        try {
            if(urlAddr.indexOf(pageSubfix)<0){
                return 0;
            }
            String end = urlAddr.substring(urlAddr.indexOf(pageSplit) + 1, urlAddr.indexOf(pageSubfix));
            return Integer.parseInt(end);
        }catch(Exception e){
            log.error("页码解析出错", e);
            return -1;
        }
    }

    public static String getUrlWithPageIndex(String urlAddr, int index){
        try {
            return urlAddr.substring(0, urlAddr.indexOf(pagePrefix))+index+urlAddr.substring(urlAddr.indexOf(pageSubfix)+1);
        }catch(Exception e){
            log.error("翻页地址转换出错", e);
            return "";
        }
    }

    public static StringBuffer getAhref(String str) {
        StringBuffer stringBufferResult = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            if (chr == '%') {
                StringBuffer stringTmp = new StringBuffer();
                stringTmp.append(str.charAt(i + 1)).append(str.charAt(i + 2));
                stringBufferResult.append((char) (Integer.valueOf(stringTmp.toString(), 16).intValue()));
                i = i + 2;
                continue;
            }
            stringBufferResult.append(chr);
        }
        return stringBufferResult;
    }

    public static void main(String[] args){

        Spider spider = Spider.create(new BaiduBaikePageProcessor()).thread(2);
        String urlTemplate = "http://baike.baidu.com/search/word?word=%s&pic=1&sug=1&enc=utf8";
        ResultItems resultItems = (ResultItems)spider.get(String.format(urlTemplate, "水力发电"));
        System.out.println(resultItems);
        List<String> list = new ArrayList();
        list.add(String.format(urlTemplate, "风力发电"));
        list.add(String.format(urlTemplate, "太阳能"));
        list.add(String.format(urlTemplate, "地热发电"));
        list.add(String.format(urlTemplate, "地热发电"));
        List<ResultItems> resultItemses = spider.getAll(list);
        Iterator var6 = resultItemses.iterator();

        while(var6.hasNext()) {
            ResultItems resultItemse = (ResultItems)var6.next();
            System.out.println(resultItemse.getAll());
        }

        spider.close();

    }
}
