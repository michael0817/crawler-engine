package com.pab.framework.crawlerengine.task;

import com.mall.cmbchina.category.get.Categories;
import com.mall.cmbchina.domain.Category;
import com.mall.cmbchina.domain.Product;
import com.mall.cmbchina.product.get.ProductList;
import com.mall.cmbchina.product.get.ProductListAjaxLoad;
import com.mall.cmbchina.product.post.html.Review;
import com.mall.cmbchina.product.post.html.Scale;
import com.mall.cmbchina.product.post.json.Desc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
@Configurable
@EnableScheduling
public class MallTaskHandler implements TaskHandler {
    @Autowired
    ExecutorService threadFactory;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    StringBuilder builder;

    //@Scheduled(cron = "0  0 8 * * ?")
    @Override
    public void taskRun() {
        threadFactory.execute(new Runnable() {
            public void run() {
                Categories categories = new Categories();
                Category category = categories.process("5");
                String subcategory = category.getSubcategory();
                ProductList productList = new ProductList();
                Product process = productList.process(subcategory);
                ProductListAjaxLoad productListAjaxLoad;
                Desc desc;
                Scale scale;
                Review review;

                productListAjaxLoad = new ProductListAjaxLoad();
                productListAjaxLoad.process(subcategory, 1 + "");
                List<String> productCodes = productListAjaxLoad.getProductCodes();
                desc = new Desc();
                scale = new Scale();
                review = new Review();

                for (String productCode : productCodes) {
                    try {
                        String descStr = desc.getDesc(productCode);
                        String scaleStr = scale.getScale(productCode);
                         builder=review.getReviewList(productCode);

                        System.err.println(builder);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
