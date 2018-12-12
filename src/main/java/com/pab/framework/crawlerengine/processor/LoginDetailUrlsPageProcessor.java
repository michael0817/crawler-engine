package com.pab.framework.crawlerengine.processor;


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

    private Site site =  Site.me().setRetryTimes(3).setSleepTime(1000)
            .addCookie("aliyungf_tc","AQAAAPAlhGGk7AMAUX2rtNO8XxgObcAJ")
            .addCookie("Hm_lpvt_1db88642e346389874251b5a1eded6e3","1544362693")
            .addCookie("Hm_lvt_1db88642e346389874251b5a1eded6e3","1544353970,1544361916")
            .addCookie("_ga","GA1.2.846058034.1544353971")
            .addCookie("_gat_gtag_UA_16079156_4","1")
            .addCookie("_gid","GA1.2.768144027.1544353971")
            .addCookie("device_id","7c763f61f88a192c5b9752ea7f98406f")
            .addCookie("u","311544361915702")
            .addCookie("xq_a_token","6125633fe86dec75d9edcd37ac089d8aed148b9e")
            .addCookie("xq_a_token.sig","CKaeIxP0OqcHQf2b4XOfUg-gXv0")
            .addCookie("xq_r_token","335505f8d6608a9d9fa932c981d547ad9336e2b5")
            .addCookie("xq_r_token.sig","i9gZwKtoEEpsL9Ck0G7yUGU42LY");



    private List<String> list = new ArrayList<String>();


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

    public List<String> process(List<String> urlAddrs) {
        Spider spider = Spider.create(this).addUrl(urlAddrs.toArray(new String[urlAddrs.size()]));
        spider.run();
        if (Spider.Status.Stopped.compareTo(spider.getStatus())==0){
            return  list;
        }
        return null;
    }

    @Override
    public Site getSite() {
        return site;
    }


}
