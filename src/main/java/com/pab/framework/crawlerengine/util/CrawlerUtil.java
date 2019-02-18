package com.pab.framework.crawlerengine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pab.framework.crawlercore.constant.Global;
import com.pab.framework.crawlerengine.model.News;

public final class CrawlerUtil {

	public static final Pattern pLink = Pattern.compile("(\\n|\\s|\\n\\s|\\s\\n)(<http|<https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?>(\\n)?");
    
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
    
    /*
     * 替换content中超链接
     */
    public static void replaceHyperLink(List<News> content){
		
		for(int i=0;i<content.size();i++){
			StringBuffer stringBuffer = new StringBuffer();
			Matcher m  =  pLink.matcher(content.get(i).getContent().get(0));
			while(m.find()){
				m.appendReplacement(stringBuffer, "");
			}
			m.appendTail(stringBuffer);
			content.get(i).setContent(Arrays.asList(stringBuffer.toString()));
		}
	}
   
    /*
     * 内容，标题关键字过滤
     */
    public static boolean filterKeyword(News news,String keyword ){
    	String[] keywords = keyword.split(",");
    	String title = news.getTitle();
    	String content = news.getContent().get(0);
    	for(String key : keywords){
    		if(title.contains(key) || content.contains(key)){
    			return true;
    	   }
    	}
       return false;
    }
    
    
    public static void main(String[] args){
        getActionId("aaa[action_id_22]");
    }
}