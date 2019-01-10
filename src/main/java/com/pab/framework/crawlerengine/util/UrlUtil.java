package com.pab.framework.crawlerengine.util;

import com.pab.framework.crawlerengine.constant.Global;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.example.BaiduBaikePageProcessor;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public final class UrlUtil {

//    public static void trustEveryone() {
//        try {
//            HttpsURLConnection.setDefaultHostnameVerifier(new UrlUtil .1 ())
//            SSLContext context = SSLContext.getInstance("TLS");
//            context.init(null, new X509TrustManager[]{new UrlUtil.2()}, new SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public static int getStartPageIndex(String urlAddr) {
        try {
            if (urlAddr.indexOf("{") < 0) {
                return 0;
            }
            String start = urlAddr.substring(urlAddr.indexOf(Global.CRAWLER_PAGE_PREFIX) + 1, urlAddr.indexOf(Global.CRAWLER_PAGE_SPLIT));
            return Integer.parseInt(start);
        } catch (Exception e) {
            log.error("页码解析出错", e);
        }
        return -1;
    }

    public static int getEndPageIndex(String urlAddr) {
        try {
            if (urlAddr.indexOf("}") < 0) {
                return 0;
            }
            String end = urlAddr.substring(urlAddr.indexOf(Global.CRAWLER_PAGE_SPLIT) + 1, urlAddr.indexOf(Global.CRAWLER_PAGE_SUBFIX));
            return Integer.parseInt(end);
        } catch (Exception e) {
            log.error("页码解析出错", e);
        }
        return -1;
    }

    public static String getUrlWithPageIndex(String urlAddr, int index) {
        try {
            return urlAddr.substring(0, urlAddr.indexOf(Global.CRAWLER_PAGE_PREFIX)) + index + urlAddr.substring(urlAddr.indexOf(Global
                    .CRAWLER_PAGE_SUBFIX) + 1);
        } catch (Exception e) {
            log.error("翻页地址转换出错", e);
        }
        return "";
    }

//    public static StringBuffer getAhref(String str) {
//        StringBuffer stringBufferResult = new StringBuffer();
//        for (int i = 0; i < str.length(); i++) {
//            char chr = str.charAt(i);
//            if (chr == '%') {
//                StringBuffer stringTmp = new StringBuffer();
//                stringTmp.append(str.charAt(i + 1)).append(str.charAt(i + 2));
//                stringBufferResult.append((char) Integer.valueOf(stringTmp.toString(), 16).intValue());
//                i += 2;
//            } else {
//                stringBufferResult.append(chr);
//            }
//        }
//        return stringBufferResult;
//    }

    public static void main(String[] args) {
        Spider spider = Spider.create(new BaiduBaikePageProcessor()).thread(2);
        String urlTemplate = "http://baike.baidu.com/search/word?word=%s&pic=1&sug=1&enc=utf8";
        ResultItems resultItems = spider.get(String.format(urlTemplate, "水力发电"));
        System.out.println(resultItems);
        List<String> list = new ArrayList();
        list.add(String.format(urlTemplate, "风力发电"));
        list.add(String.format(urlTemplate, "太阳能"));
        list.add(String.format(urlTemplate, "地热发电"));
        list.add(String.format(urlTemplate, "地热发电"));
        List<ResultItems> resultItemses = spider.getAll(list);
        while (resultItemses.iterator().hasNext()) {
            ResultItems resultItems1 = resultItemses.iterator().next();
            System.out.println(resultItems.getAll());
        }

        spider.close();
    }
}