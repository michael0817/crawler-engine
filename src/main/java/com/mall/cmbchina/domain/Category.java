package com.mall.cmbchina.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;

public class Category  extends BaseEntity {
    private  String subcategory;
    private String categoryName;
    private int dateId;

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }

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
