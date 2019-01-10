//package com.pab.framework.crawlerengine.processor;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.pab.framework.crawlerengine.util.UrlUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import us.codecraft.webmagic.Page;
//import us.codecraft.webmagic.Site;
//import us.codecraft.webmagic.Spider;
//import us.codecraft.webmagic.processor.PageProcessor;
//import us.codecraft.webmagic.selector.Html;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Component
//public class DetailUrlsPageProcessor implements PageProcessor {
//
//    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000)
//            .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
//            .addHeader("Content-Type", "application/x-www-form-urlencoded");
////            .addCookie("aliyungf_tc","AQAAAPAlhGGk7AMAUX2rtNO8XxgObcAJ")
////            .addCookie("Hm_lpvt_1db88642e346389874251b5a1eded6e3","1544362693")
////            .addCookie("Hm_lvt_1db88642e346389874251b5a1eded6e3","1544353970,1544361916")
////            .addCookie("_ga","GA1.2.846058034.1544353971")
////            .addCookie("_gat_gtag_UA_16079156_4","1")
////            .addCookie("_gid","GA1.2.768144027.1544353971")
////            .addCookie("device_id","7c763f61f88a192c5b9752ea7f98406f")
////            .addCookie("u","311544361915702")
////            .addCookie("xq_a_token","6125633fe86dec75d9edcd37ac089d8aed148b9e")
////            .addCookie("xq_a_token.sig","CKaeIxP0OqcHQf2b4XOfUg-gXv0")
////            .addCookie("xq_r_token","335505f8d6608a9d9fa932c981d547ad9336e2b5")
////            .addCookie("xq_r_token.sig","i9gZwKtoEEpsL9Ck0G7yUGU42LY");
//
//    private List<String> all=null;
//    private String regex;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    public void setRegex(String regex) {
//        this.regex = regex;
//    }
//
//    @Override
//    public void process(Page page) {
//
//        all=new LinkedList<>();
//        page.setCharset("utf-8");
//        String contentType=  page.getHeaders().get("Content-Type").get(0);
//        if (contentType.contains("html")){
//            Html html = page.getHtml();
//            all.addAll(html.xpath("a/@href").all());
//            if (StringUtils.isNotEmpty(regex)) {
//                all = all.stream().filter(a -> a.matches(regex)).collect(Collectors.toList());
//            }
//            else {
//                //手机数据暂时逻辑
//                int size = all.size();
//                String href;
//                for (int i = 0; i < size; i++) {
//                    href = all.get(i);
//                    href = UrlUtils.getAhref(href).delete(0, "cmblife://go?url=web&next=https://ssl.mall.cmbchina.com".length()).toString();
//                    all.set(i, href);
//                }
//            }
//        }
//        else if(contentType.contains("json")){
//            JSONArray statuses = JSONObject.parseObject(page.getRawText()).getJSONArray("statuses");
//            int size = statuses.size();
//            JSONObject jsonObject;
//            for (int i = 0; i < size; i++) {
//                jsonObject = statuses.getJSONObject(i);
//                all.add(jsonObject.getString("target"));
//            }
//        }
//        else{
//            String[] urls = page.getUrl().get().split(",");
//            String url;
//            for (int i=0;i<urls.length;i++){
//                url=urls[i];
//                int index = url.lastIndexOf("=");
//                int pageSize=   Integer.parseInt(url.substring(index+1));
//                pageSize*=6;
//                url=url.substring(0,index+1)+pageSize;
//                all.add(url);
//            }
//        }
//
//
//    }
//
//    public List process(String regex, List<String> urlAddrs) {
//        if (StringUtils.isNotEmpty(regex)) {
//            this.setRegex(regex);
//        }
//        Spider spider = Spider.create(this).thread(urlAddrs.size()).addUrl(urlAddrs.toArray(new String[urlAddrs.size()]));
//        spider.run();
//        if (Spider.Status.Stopped.compareTo(spider.getStatus()) == 0) {
//            return all;
//        }
//        return null;
//    }
//
//    @Override
//    public Site getSite() {
//        return site;
//    }
//
//}
