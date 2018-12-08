package com.pab.framework.crawlerengine.processor.action;

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
import com.pab.framework.crawlerdb.dao.CrawlerActionInfoDao;
import com.pab.framework.crawlerdb.dao.CrawlerNewsMilestoneDao;
import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import com.pab.framework.crawlerengine.crawler.CrawlerHandler;
import com.pab.framework.crawlerengine.processor.DetailPageProcessor;
import com.pab.framework.crawlerengine.processor.DetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.processor.LoginDetailUrlsPageProcessor;
import com.pab.framework.crawlerengine.util.FileUtils;
import com.pab.framework.crawlerengine.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author xumx
 * @date 2018/11/16
 */
@Component
public class NewsActionProcessorImpl implements ActionProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DetailUrlsPageProcessor detailUrlsPageProcessor;
    @Autowired
    private DetailPageProcessor detailPageProcessor;
    @Autowired
    private LoginDetailUrlsPageProcessor loginDetailUrlsPageProcessor;
    @Autowired
    private CrawlerActionInfoDao crawlerActionInfoDao;
    @Autowired
    private CrawerActionDynamicInfoDao crawerActionDynamicInfoDao;
    @Autowired
    private CrawlerNewsMilestoneDao crawlerNewsMilestoneDao;
    @Autowired
    private CrawlerHandler crawlerHandler;
    private List<String> urlAddrs = null;

    @Override
    public List<String> getUrlAddrs(CrawlerActionInfo crawlerActionInfo) {
        String crawlerRegex = crawlerActionInfo.getCrawlerRegex();
        String urlAddr = crawlerActionInfo.getUrlAddr();
        String prefix = urlAddr.substring(0, urlAddr.indexOf("{"));
        String suffix = urlAddr.substring(urlAddr.indexOf("}") + 1);
        int index = UrlUtils.maxPage(urlAddr);
        String baseUrlAddr = crawlerActionInfo.getBaseUrlAddr();
        for (int i = 1; i <= index; i++) {
            if ("https://xueqiu.com".equals(baseUrlAddr)) {
                prefix = "/v4/statuses/user_timeline.json?page=";
                suffix = "&user_id=7558914709";
                loginDetailUrlsPageProcessor.process(baseUrlAddr + prefix + i + suffix);
                return loginDetailUrlsPageProcessor.getList();
            }
            else {
                detailUrlsPageProcessor.setRegex(crawlerRegex);
                detailUrlsPageProcessor.process(crawlerRegex, baseUrlAddr + prefix + i + suffix);
                return detailUrlsPageProcessor.getList();
            }

        }
        return null;
    }

    @Override
    public void process(Integer actionId) throws IOException {
        CrawlerActionInfo crawlerActionInfo = crawlerActionInfoDao.findOneByActionById(actionId);
        if (actionId<27){
            int actionType = crawlerActionInfo.getActionType();
            switch (actionType) {
                case 2:
                    urlAddrs = getUrlAddrs(crawlerActionInfo);
                    break;
                case 3:
                    crawlerHandler.handler(crawlerActionInfo,urlAddrs);
                    break;
            }
        }
        //暂时处理商城手机
        else{
            Categories categories = new Categories();
            Category category = categories.process("5");
            String categoryName=category.getCategoryName();
            String subcategory = category.getSubcategory();
            ProductList productList=new ProductList();
            Product product = productList.process(subcategory);
            ProductListAjaxLoad productListAjaxLoad = new ProductListAjaxLoad();
            productListAjaxLoad.process(subcategory, 1 + "");
            List<String> productCodes = productListAjaxLoad.getProductCodes();
            StringBuilder reviewBuilder=null;
            StringBuilder consultBuilder=null;
            for (String productCode : productCodes) {
                try {
                    String descStr = Desc.getDesc(productCode);
                    String scaleStr = Scale.getScale(productCode);
                    if (descStr!=null){
                        FileUtils.write(FileUtils.getDir()+"图文参数("+categoryName+")"+".txt",descStr);
                    }
                    if (scaleStr!=null){
                        FileUtils.write(FileUtils.getDir()+"产品参数("+categoryName+")"+".txt",scaleStr);
                    }
                    reviewBuilder = Review.htmlBuilder(productCode);
                    if (reviewBuilder!=null) {
                        FileUtils.write(FileUtils.getDir()+"咨询("+categoryName+")"+".txt",reviewBuilder.toString());
                    }
                    consultBuilder= Consult.htmlBuilder(productCode);

                    if (consultBuilder!=null){
                        FileUtils.write(FileUtils.getDir()+"评论("+categoryName+")"+".txt",consultBuilder.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


    }


}