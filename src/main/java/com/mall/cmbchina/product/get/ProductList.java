package com.mall.cmbchina.product.get;

import com.mall.cmbchina.domain.Product;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class ProductList implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private String subCategory;
    private String totalPageNumber;

    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Document document = html.getDocument();
        subCategory = document.getElementById("subCategory").val();
        totalPageNumber = document.getElementById("totalPageNumber").val();
    }


    public Product process(String subcategory) {
        Spider spider = Spider.create(this).addUrl("https://ssl.mall.cmbchina.com/_CL5_/Product/ProductList?subcategory=" + subcategory + "&pushwebview=1").thread(5);
        spider.run();
        if (Spider.Status.Stopped.compareTo(Spider.Status.Stopped) == 0) {
            Product product = new Product();
            product.setSubCategory(subCategory);
            product.setTotalPageNumber(totalPageNumber);
            return product;
        }
        return null;
    }

    @Override
    public Site getSite() {
        return site;
    }



}
