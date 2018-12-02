package com.pab.framework.crawlerengine.crawler.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CrawlerUtil {
    public static int countWords(String str,String word)
    {
        int count = 0;
        for (int i = str.length()-1 ;i>=0;--i) {
            if (str.regionMatches(i, word, 0, word.length())) {
                ++count;
            }
        }
        return count;
    }

    public static  String getText(Element element){
        Elements children = element.children();
        int size=children.size();
        if (!children.isEmpty()){
            for (int i = 0; i <size; i++) {
               element=children.get(i);
                getText(element);
            }
        }
        return element.text();
    }

    public static String domainStr(String url) {
        return url.split("/")[2];
    }

    public static String htmlTransStr(String str) {
        Pattern pattern = Pattern.compile(">[^<]+<");
        Matcher matcher = pattern.matcher(str);
        StringBuilder builder = new StringBuilder(str.length());
        if (matcher.find()) {
            str = matcher.group();
            builder.append(str);
        }
        str = builder.toString();
        str = str.trim();
        if (!str.isEmpty()) {
            str = str.substring(1, str.length() - 1);
            str = StringEscapeUtils.unescapeHtml(str);
            str=str.replaceAll("<>","");
            return str;
        }
        return null;
    }

    /**
     * 抽取字符串的数字
     *
     * @return
     */
    public static int extractIntOfStr(String str, String begin, String end) {
        if (str == null) {
            return -1;
        }
        int index = Integer.parseInt(str.substring(str.indexOf(begin) + 1, str.indexOf(end)));
        return index;
    }

    /**
     * 根据页面分析，转码后汉字解码
     *
     * @param inputst     12345625E61234562589123456258B12345625E6123456259C12345625BA
     * @param regex       "123456"
     * @param replacement "%"
     * @return 手机
     * @throws Exception
     */
    public static String hanDecode(String inputst, String regex, String replacement) throws Exception {
        //String ed = "12345625E812345625BD12345625A612345625E512345625851234562585/12345625E51234562585123456258512345625E7123456259412345625B512345625E5123456259912345625A8";
        String ed2 = inputst.replaceAll(regex, replacement);
        String ed3 = URLDecoder.decode(ed2, "UTF-8");
        return URLDecoder.decode(ed3, "UTF-8");
    }

    /**
     * 根据页面分析，汉字转码，拼接参数页面跳转
     *
     * @param inputst     手机
     * @param regex       "%"
     * @param replacement "123456"
     * @return 12345625E61234562589123456258B12345625E6123456259C12345625BA
     * @throws Exception
     */
    public static String decodeHan(String inputst, String regex, String replacement) throws Exception {
        //String ed = "车充/充电器"; // 手机//12345625E812345625BD12345625A612345625E512345625851234562585/12345625E51234562585123456258512345625E7123456259412345625B512345625E5123456259912345625A8
        String sout = "";
        if (inputst.contains("/")) {
            String[] sts = inputst.split("/");
            for (int i = 0; i < sts.length; i++) {
                sout += URLEncoder.encode(sts[i], "UTF-8");
                sout += "/";

            }
            sout = sout.substring(0, sout.length() - 1);
        } else {
            sout = URLEncoder.encode(inputst, "UTF-8");
        }
        return sout.replaceAll(regex, replacement);
    }

    /**
     * 转换url
     *
     * @param str https%3a%2f%2fssl.mall.cmbchina.com%2f_CL5_%2fProduct%2f
     * @return https://ssl.mall.cmbchina.com/_CL5_/Product/
     */
    public static StringBuffer getAhref(String str) {
        //String str = "https%3a%2f%2fssl.mall.cmbchina.com%2f_CL5_%2fProduct%2fDetail%3fproductCode%3dS1H-70D-23E_026%26pushwebview%3d1%26productIndex%3d1";
        StringBuffer stringBufferResult = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            if (chr == '%') {
                StringBuffer stringTmp = new StringBuffer();
                stringTmp.append(str.charAt(i + 1)).append(str.charAt(i + 2));
                //转换字符，16进制转换成整型
                stringBufferResult.append((char) (Integer.valueOf(stringTmp.toString(), 16).intValue()));
                i = i + 2;
                continue;
            }
            stringBufferResult.append(chr);
        }
        return stringBufferResult;
    }

}
