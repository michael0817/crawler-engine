package com.pab.framework.crawlerengine.enums;

public enum UrlTypeEnum {
    HTML(1),
    JSON(2),
    TXT(3);

    private int label;

    UrlTypeEnum(Integer label){
        this.label = label;
    }

    public int getLabel(){
        return this.label;
    }
}
