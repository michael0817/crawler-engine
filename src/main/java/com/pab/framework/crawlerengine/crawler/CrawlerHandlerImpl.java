package com.pab.framework.crawlerengine.crawler;

import com.mall.cmbchina.category.get.Categories;
import com.mall.cmbchina.domain.Category;
import com.mall.cmbchina.domain.Product;
import com.mall.cmbchina.product.get.ProductList;
import com.mall.cmbchina.product.get.ProductListAjaxLoad;
import com.mall.cmbchina.product.post.html.Consult;
import com.mall.cmbchina.product.post.html.Review;
import com.mall.cmbchina.product.post.html.Scale;
import com.mall.cmbchina.product.post.json.Desc;
import com.pab.framework.crawlerdb.dao.CrawerActionDynamicInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerNewsMilestoneDao;
import com.pab.framework.crawlerdb.domain.CrawerActionDynamicInfo;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerdb.domain.CrawlerArticle;
import com.pab.framework.crawlerdb.domain.CrawlerNewsMilestone;
import com.pab.framework.crawlerengine.processor.DetailPageProcessor;
import com.pab.framework.crawlerengine.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
public class CrawlerHandlerImpl implements CrawlerHandler {
    @Autowired
    private DetailPageProcessor detailPageProcessor;
    @Autowired
    private CrawlerNewsMilestoneDao crawlerNewsMilestoneDao;
    @Autowired
    private CrawerActionDynamicInfoDao crawerActionDynamicInfoDao;

    @Override
    public void handler(CrawlerActionInfo crawlerActionInfo, List<String> urlAddrs) throws IOException {

        String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();
        Integer urlType = crawlerActionInfo.getUrlType();
        String urlAddr = crawlerActionInfo.getUrlAddr();
        int actionId=crawlerActionInfo.getActionId();
        int actionType = crawlerActionInfo.getActionType();
        crawerActionDynamicInfoDao.deleteAll(actionId);
        int size = urlAddrs.size();
        CrawerActionDynamicInfo crawerActionDynamicInfo;
        CrawlerNewsMilestone crawlerNewsMilestone;
        List<CrawlerArticle> crawlerArticles;
        String actionDesc;
        for (int i = 0; i < size; i++) {
            crawerActionDynamicInfo = new CrawerActionDynamicInfo();
            crawerActionDynamicInfo.setActionId(actionId);
            crawerActionDynamicInfo.setActionType(actionType);
            crawerActionDynamicInfo.setUrlType(urlType);
            crawerActionDynamicInfo.setUrlAddr(urlAddrs.get(i));
            crawerActionDynamicInfoDao.insertAll(crawerActionDynamicInfo);
            crawlerNewsMilestone = new CrawlerNewsMilestone();
            crawlerNewsMilestone.setActionId(actionId);
            crawlerNewsMilestone.setUrlAddr(urlAddrs.get(i));
            int existsUrl = crawlerNewsMilestoneDao.isExistsUrl(crawlerNewsMilestone);
            if (existsUrl == 0) {
                crawlerNewsMilestoneDao.insertAll(crawlerNewsMilestone);
            }
        }
       List<String> addrs = crawlerNewsMilestoneDao.findUrlAddrsByNewDate(actionId);
        //商城手机暂时逻辑
        if ("https://ssl.mall.cmbchina.com".equals(baseUrlAddr)) {
            Categories categories = new Categories();
            Category category = categories.process("5");
            String categoryName = category.getCategoryName();
            String subcategory = category.getSubcategory();
            ProductList productList = new ProductList();
            Product product = productList.process(subcategory);
            ProductListAjaxLoad productListAjaxLoad = new ProductListAjaxLoad();
            productListAjaxLoad.process(subcategory, 1 + "");
            List<String> productCodes = productListAjaxLoad.getProductCodes();
            StringBuilder reviewBuilder = null;
            StringBuilder consultBuilder = null;
            for (String productCode : productCodes) {
                try {
                    String descStr = Desc.getDesc(productCode);
                    String scaleStr = Scale.getScale(productCode);
                    if (descStr != null) {
                        FileUtils.write(FileUtils.getDir() + "图文参数(" + categoryName + ")" + ".txt", descStr);
                    }
                    if (scaleStr != null) {
                        FileUtils.write(FileUtils.getDir() + "产品参数(" + categoryName + ")" + ".txt", scaleStr);
                    }
                    reviewBuilder = Review.htmlBuilder(productCode);
                    if (reviewBuilder != null) {
                        FileUtils.write(FileUtils.getDir() + "咨询(" + categoryName + ")" + ".txt", reviewBuilder.toString());
                    }
                    consultBuilder = Consult.htmlBuilder(productCode);

                    if (consultBuilder != null) {
                        FileUtils.write(FileUtils.getDir() + "评论(" + categoryName + ")" + ".txt", consultBuilder.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            actionDesc = crawlerActionInfo.getActionDesc();
            crawlerArticles = detailPageProcessor.process(baseUrlAddr, addrs);
            FileUtils.write(actionDesc,crawlerArticles);
        }


    }
}
