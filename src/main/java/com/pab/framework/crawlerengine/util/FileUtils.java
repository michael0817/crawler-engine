//package com.pab.framework.crawlerengine.util;
//
//import com.pab.framework.crawlerdb.domain.CrawlerArticle;
//import org.apache.commons.lang3.time.DateFormatUtils;
//
//import java.io.*;
//import java.util.Date;
//import java.util.List;
//
//public final class FileUtils {
//
//    public static final String getJson(String path) throws IOException {
//        InputStream is = new FileInputStream(path);
//        InputStreamReader isr = new InputStreamReader(is);
//        BufferedReader br = new BufferedReader(isr);
//        String line;
//        StringBuilder sb = new StringBuilder();
//        while ((line = br.readLine()) != null) {
//            sb.append(line);
//        }
//        br.close();
//        isr.close();
//        is.close();
//        return sb.toString();
//    }
//
//    public static final void write(String filePath, String content) throws IOException {
//        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
//        bw.write(content);
//        if (bw != null) {
//            bw.close();
//        }
//    }
//
//    public static final void write(String actionDesc, List<CrawlerArticle> crawlerArticles) throws IOException {
//
//        actionDesc += DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss");
//        actionDesc += ".txt";
//        String filePath =System.getProperty("user.dir")+ File.separator + "out" + File.separator + actionDesc;
//        int size = crawlerArticles.size();
//        CrawlerArticle crawlerArticle;
//        StringBuilder builder = new StringBuilder();
//        String title;
//        String  date;
//        String content;
//        for (int i = 0; i < size; i++) {
//            crawlerArticle = crawlerArticles.get(i);
//            builder.append("第" + (i + 1) + "篇文章开始\n");
//            title = crawlerArticle.getTitle();
//            if (title != null) {
//                builder.append(title);
//                builder.append("\n");
//            }
//            date = crawlerArticle.getDate();
//            if (date != null) {
//                builder.append(title);
//                builder.append("\n");
//            }
//            content = crawlerArticle.getContent();
//            if (content != null) {
//                builder.append(content);
//                builder.append("\n");
//            }
//            builder.append("第" + (i + 1) + "篇文章结束\n\n");
//        }
//
//        if (crawlerArticles != null) {
//            FileUtils.write(filePath, builder.toString());
//        }
//    }
//
//    public static  String getDir(){
//        return  System.getProperty("user.dir")+File.separator+"out"+File.separator;
//    }
//}
