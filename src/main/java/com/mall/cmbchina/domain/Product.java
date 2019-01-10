package com.mall.cmbchina.domain;

import com.pab.framework.crawlerdb.common.BaseEntity;

public class Product extends BaseEntity {

    private String subCategory;
    private String totalPageNumber;

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getTotalPageNumber() {
        return totalPageNumber;
    }

    public void setTotalPageNumber(String totalPageNumber) {
        this.totalPageNumber = totalPageNumber;
    }
}
