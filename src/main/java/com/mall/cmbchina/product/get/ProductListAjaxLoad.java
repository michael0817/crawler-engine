package com.mall.cmbchina.product.get;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

public class ProductListAjaxLoad implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    private StringBuilder builder;
    private  List<String> productCodes=new ArrayList<>();
    @Override
    public void process(Page page) {
        Html html = page.getHtml();
        Selectable xpath = html.xpath("div[@class='group-buy-item']");
        List<Selectable> nodes = xpath.nodes();
        StringBuilder builder = new StringBuilder();
        for (Selectable node : nodes) {
            builder.append(node.get());
            productCodes.add( node.xpath("span[@productid]/@productid").get());
        }

    }

    public StringBuilder getBuilder() {
        return  Spider.Status.Stopped.compareTo(Spider.Status.Stopped) == 0?builder:null;
    }

    public List<String> getProductCodes() {
        return   Spider.Status.Stopped.compareTo(Spider.Status.Stopped) == 0?productCodes:null;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void process(String subCategory, String pageIndex) {
        Spider spider = Spider.create(this).addUrl("https://ssl.mall.cmbchina.com/_CL5_/Product/ProductList?subcategory=" + subCategory + "&pageIndex=" + pageIndex).thread(5);
        spider.run();

    }

    public static void main(String[] args) {
        ProductListAjaxLoad productListAjaxLoad = new ProductListAjaxLoad();
        productListAjaxLoad.process("370", "1");
    }
}
