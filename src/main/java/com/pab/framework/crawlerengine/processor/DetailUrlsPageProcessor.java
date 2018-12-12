package com.pab.framework.crawlerengine.processor;

import com.pab.framework.crawlerengine.util.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Html;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class DetailUrlsPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private List<String> all=null;
    private String regex;

    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public void process(Page page) {
        all=new LinkedList<>();
        page.setCharset("utf-8");
        Html html = page.getHtml();
        all.addAll(html.xpath("a/@href").all());
        if (StringUtils.isNotEmpty(regex)) {
            all = all.stream().filter(a -> a.matches(regex)).collect(Collectors.toList());
        } else {
            //手机数据暂时逻辑
            int size = all.size();
            String href;
            for (int i = 0; i < size; i++) {
                href = all.get(i);
                href = UrlUtils.getAhref(href).delete(0, "cmblife://go?url=web&next=https://ssl.mall.cmbchina.com".length()).toString();
                all.set(i, href);
            }
        }

    }

    public List process(String regex, List<String> urlAddrs) {
        if (StringUtils.isNotEmpty(regex)) {
            this.setRegex(regex);
        }
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("106.42.25.225", 80),
                new Proxy("117.21.219.73", 80),
                new Proxy("118.212.233.123", 80)

        ));
        Spider spider = Spider.create(this).setDownloader(httpClientDownloader).thread(urlAddrs.size()).addUrl(urlAddrs.toArray(new String[urlAddrs.size()]));
        spider.run();
        if (Spider.Status.Stopped.compareTo(spider.getStatus()) == 0) {
            return all;
        }
        return null;
    }

    @Override
    public Site getSite() {
        return site;
    }

}
