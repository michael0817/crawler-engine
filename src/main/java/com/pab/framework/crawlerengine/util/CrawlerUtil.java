package com.pab.framework.crawlerengine.util;

import com.pab.framework.crawlercore.constant.Global;

import java.util.List;

public final class CrawlerUtil {

    public static void replaceDynamicContent(String urlPattern, List<String> contentList){

    }

    public static int getActionId(String urlPattern) throws NumberFormatException{
        String action = urlPattern.substring(urlPattern.indexOf(Global.DYNAMIC_ACTION_PREFIX) + 1, urlPattern.indexOf
                (Global.DYNAMIC_ACTION_SUBFIX));
        return Integer.parseInt(action.replace(Global.DYNAMIC_ACTION_KEYWORD,""));

    }
}