package com.mall.cmbchina.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;

public class Category  extends BaseEntity {
    private  String subcategory;
    private String categoryName;

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
