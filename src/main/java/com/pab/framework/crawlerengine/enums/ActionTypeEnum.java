package com.pab.framework.crawlerengine.enums;

public enum ActionTypeEnum {

    CLICK(1),
    TURNPAGE(2),
    COOKIE(3),
    NEWS(4),
	MALL(5);

    private int label;

    ActionTypeEnum(int label) {
        this.label = label;
    }

    public int getLabel() {
        return this.label;
    }
}
