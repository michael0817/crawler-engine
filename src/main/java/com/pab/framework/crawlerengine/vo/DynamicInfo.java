package com.pab.framework.crawlerengine.vo;

import lombok.Data;

@Data
/**
 * xumx
 * 动态爬取的内容
 */
public class DynamicInfo {
    //标题
    private String article;
    //内容（目前存放url）
    private String content;
}