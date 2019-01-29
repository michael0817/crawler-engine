package com.pab.framework.crawlerengine.crawler;

import com.pab.framework.crawlerengine.service.ProxyService;
import com.pab.framework.crawlerengine.util.HttpUtil;
import com.pab.framework.crawlerengine.model.CrawlerJobInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


@Component
@Slf4j
public class CrawlerAddressHandlerImpl implements CrawlerHandler {

    @Autowired
    ProxyService proxyService;

    public static void main(String[] args)
            throws Exception {

//        CrawlerAddressHandlerImpl chi = new CrawlerAddressHandlerImpl();
//        CrawlerJobInfo cji = new CrawlerJobInfo();
//        List<String> urls = new ArrayList();
//
//        urls.add("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/31.html");
//        cji.setUrls(urls);
//        chi.handler(cji);

        File folder = new File("/Users/fjn/Desktop/www.stats.gov.cn");
        File outputFile = new File("/Users/fjn/Desktop/output.txt");
        FileOutputStream fos = new FileOutputStream(outputFile);
        try {
            if (folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    Scanner s = new Scanner(file);
                    while (s.hasNextLine()) {
                        String line = s.nextLine();
                        if ((!line.contains("url")) && (!line.contains("title")) && (!line.contains("虚拟"))) {
                            fos.write((line.replaceAll("村民委员会", "").replaceAll("村委会", "").replaceAll("居委会", "") +
                                    "\r\n").getBytes());
                        }
                    }
                }
            }
        } finally {
            fos.close();
        }
    }

    public List<Object> handler(CrawlerJobInfo crawlerJobInfo) {
        Spider spider = Spider.create(this).thread(20).addPipeline(new us.codecraft.webmagic.pipeline.FilePipeline("/Users/fjn/Desktop"));
        List<Object> targetUrlList = new ArrayList();
        for (Object obj : crawlerJobInfo.getGetUrls()) {
            spider.addUrl(new String[]{(String) obj}).run();
        }
        return targetUrlList;
    }

    public void process(Page page) {
        try {
            for (String s : page.getHtml().xpath("//tr[@class='provincetr']//a/@href").all()) {
                page.addTargetRequest(page.getUrl() + "/" + s);
                page.putField("title", page.getHtml().xpath("//tr[@class='provincetr']//a/text()").all());
            }
            for (String s : page.getHtml().xpath("//tr[@class='citytr']//td[1]/a/@href").all()) {
                page.addTargetRequest("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/" + s);
                page.putField("title", page.getHtml().xpath("//tr[@class='citytr']//td[2]/a/text()").all());
            }
            for (String s : page.getHtml().xpath("//tr[@class='countytr']//td[1]/a/@href").all()) {
                String url = page.getUrl().toString();
                page.addTargetRequest(url.substring(0, url.lastIndexOf("/")) + "/" + s);
                page.putField("title", page.getHtml().xpath("//tr[@class='countytr']//td[2]/a/text()").all());
            }
            for (String s : page.getHtml().xpath("//tr[@class='towntr']//td[1]/a/@href").all()) {
                String url = page.getUrl().toString();
                page.addTargetRequest(url.substring(0, url.lastIndexOf("/")) + "/" + s);
                page.putField("title", page.getHtml().xpath("//tr[@class='towntr']//td[2]/a/text()").all());
            }
            if (page.getHtml().xpath("//tr[@class='villagetr']//td[3]/text()").all().size() > 0) {
                page.putField("title", page.getHtml().xpath("//tr[@class='villagetr']//td[3]/text()").all());
            }
        } catch (Exception e) {
            log.error("处理爬取内容失败:" + page.getUrl(), e);
        }
    }

    public Site getSite() {
        Site site = Site.me().setRetryTimes(3).setSleepTime(500).setRetrySleepTime(500).setTimeOut(20000).setUseGzip(true).addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

        List<Cookie> list = HttpUtil.getCookies("http://www.stats.gov.cn");
        for (Cookie c : list) {
            site.addCookie(c.getName(), c.getValue());
        }
        return site;
    }

}