package com.pab.framework.crawlerengine.util;

import com.pab.framework.crawlercore.constant.Global;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CrawlerUtil {

    public static void replaceDynamicContent(String urlPattern, List<String> contentList){
        for(int i = 0; i < contentList.size(); i++){
            String url = urlPattern.replaceAll(Global.DYNAMIC_ACTION_REGEX, contentList.get(i));
            contentList.set(i, url);
        }
    }

    public static int getActionId(String urlPattern) throws NumberFormatException{
        Pattern c = Pattern.compile(Global.DYNAMIC_ACTION_REGEX);
        Matcher mc = c.matcher(urlPattern);
        if(mc.find()){
            String action = mc.group();
            c = Pattern.compile("\\d+");
            mc = c.matcher(action);
            if(mc.find()){
                return Integer.parseInt(mc.group());
            }
        }
        return -1;

    }

    public static void main(String[] args){
        getActionId("aaa[action_id_22]");
    }
}