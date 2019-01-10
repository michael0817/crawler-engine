package com.pab.framework.crawlerengine.factory;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.Page;

public class IresearchDetail implements Detail {

    @Override
    public String getTitle(Page page) {
        JSONObject jsonObject = JSON.parseObject(page.getRawText());
        final JSONArray jsonArray = jsonObject.getJSONArray("List");
        jsonObject = jsonArray.getJSONObject(0);
        return jsonObject.getString("Title");
    }

    @Override
    public String getDate(Page page) {
        JSONObject jsonObject = JSON.parseObject(page.getRawText());
        final JSONArray jsonArray = jsonObject.getJSONArray("List");
        jsonObject = jsonArray.getJSONObject(0);
        return jsonObject.getString("Uptime");
    }

    @Override
    public String getContent(Page page) {
        JSONObject jsonObject = JSON.parseObject(page.getRawText());
        final JSONArray jsonArray = jsonObject.getJSONArray("List");
        jsonObject = jsonArray.getJSONObject(0);
        return jsonObject.getString("Content");
    }
}
