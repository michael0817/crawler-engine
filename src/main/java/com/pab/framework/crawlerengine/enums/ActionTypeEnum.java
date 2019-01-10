package com.pab.framework.crawlerengine.enums;

public enum ActionTypeEnum {

    CLICK(Integer.valueOf(1)),
    TURNPAGE(Integer.valueOf(2)),
    COOKIE(Integer.valueOf(3)),
    NEWS(Integer.valueOf(4));

    private Integer label;

    ActionTypeEnum(Integer label) {
        this.label = label;
    }

    public Integer getLabel() {
        return this.label;
    }
}
