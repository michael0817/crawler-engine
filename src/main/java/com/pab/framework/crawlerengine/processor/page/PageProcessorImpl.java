//package com.pab.framework.crawlerengine.processor.page;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import us.codecraft.webmagic.Page;
//import us.codecraft.webmagic.Site;
//import us.codecraft.webmagic.Spider;
//import us.codecraft.webmagic.processor.PageProcessor;
//import us.codecraft.webmagic.selector.Html;
//
//@Component
//@Slf4j
//public class PageProcessorImpl implements PageProcessor {
//    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000)
//            .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)")
//            .addHeader("Content-Type", "application/x-www-form-urlencoded");
//
//    @Override
//    public void process(Page page) {
//        try {
//            Html html = page.getHtml();
//            System.out.println(html);
//        } catch (Exception e) {
//            log.error("爬取出错:" + page.getUrl(), e);
//        }
//    }
//
//    @Override
//    public Site getSite() {
//        return site;
//    }
//
//    public void spiderRun(String url) {
////        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
////        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
////                new Proxy("35.198.185.132",80),
////                new Proxy("178.128.183.164",8080)
////        ));
//
//        //   new Proxy("183.52.150.248",61234)));//120.132.9.178
//        Spider spider1 = Spider.create(this)
//                //.setScheduler(new FileCacheQueueScheduler("D:\\util\\hanlp-1.5.1-release\\trunk\\baikeFenlei\\new"))
//                // .setDownloader(httpClientDownloader)
////                .setScheduler(new QueueScheduler()
////                .setDuplicateRemover(new BloomFilterDuplicateRemover(900000000)))
//                .addUrl(url)
//                .thread(8);
//        spider1.run();
//    }
//
//}
