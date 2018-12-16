package com.mall.cmbchina.category.get;

import com.mall.cmbchina.domain.Category;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class Categories implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
   private  String subcategory;
    private String categoryName;
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        System.out.println(html);
        Document document = html.getDocument();
        Element element = document.getElementsByAttribute("c3id").get(0);
         subcategory =element.attr("c3id");
         categoryName=element.text();

    }

    @Override
    public Site getSite() {
        return site;
    }

    public Category process(String id) {
        Spider spider = Spider.create(this).addUrl("https://ssl.mall.cmbchina.com/_CL5_/Category/GetCategories?id="+id).thread(5);
        spider.run();
        if(Spider.Status.Stopped.compareTo(spider.getStatus())==0){
            Category category=new Category();
            category.setCategoryName(categoryName);
            category.setSubcategory(subcategory);
            return  category;
        }
        return null;
    }

    public static void main(String[] args) {
        Categories categories = new Categories();
        categories.process("5");
    }
}
