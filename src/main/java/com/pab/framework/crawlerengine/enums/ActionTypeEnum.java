package com.pab.framework.crawlerengine.enums;

public enum ActionTypeEnum {
    CLICK(1),
    TURNPAGE(2),
    HTML(3),
    COOKIE(4);

    private Integer label;

    ActionTypeEnum(Integer label){
        this.label = label;
    }

    public Integer getLabel(){
        return this.label;
    }
}
