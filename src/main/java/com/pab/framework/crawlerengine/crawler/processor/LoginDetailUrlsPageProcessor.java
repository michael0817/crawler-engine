package com.pab.framework.crawlerengine.crawler.processor;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;


@Component
public class LoginDetailUrlsPageProcessor implements PageProcessor {

    private Site site = createSite();
    private List<String> list = new ArrayList<String>();

    public List<String> getList() {

        return list;
    }


    public Site createSite() {
        return Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000)
                .addCookie("aliyungf_tc", "AQAAANWiBjgipQgAhn5aZSdo3aHqg8gQ")
                .addCookie("xq_a_token", "6125633fe86dec75d9edcd37ac089d8aed148b9e")
                .addCookie("xq_a_token.sig", "CKaeIxP0OqcHQf2b4XOfUg-gXv0")
                .addCookie("xq_r_token", "i9gZwKtoEEpsL9Ck0G7yUGU42LY")
                .addCookie("Hm_lvt_1db88642e346389874251b5a1eded6e3", "1543823709")
                .addCookie("Hm_lpvt_1db88642e346389874251b5a1eded6e3", "1543823709")
                .addCookie("u", "921543823709165")
                .addCookie("_ga", "GA1.2.877415006.1543823710")
                .addCookie("_gid", "GA1.2.1908579160.1543823710")
                .addCookie("device_id", "7c763f61f88a192c5b9752ea7f98406f");

    }

    @Override
    public void process(Page page) {
        page.setCharset("utf-8");
        JSONArray statuses = JSONObject.parseObject(page.getRawText()).getJSONArray("statuses");
        int size = statuses.size();
        JSONObject jsonObject;
        for (int i = 0; i < size; i++) {
            jsonObject = statuses.getJSONObject(i);
            list.add(jsonObject.getString("target"));
        }

    }

    public void process(String urlAddr) {
        Spider spider = Spider.create(this).addUrl(urlAddr);
        spider.run();

    }

    @Override
    public Site getSite() {
        return site;
    }


}
