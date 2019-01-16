package com.pab.framework.crawlerengine.enums;

public enum FlowTypeEnum {

    NEWS(1),
    PRODUCT(2),
    FINANCE(3);

    private int label;

    FlowTypeEnum(int label) {
        this.label = label;
    }

    public int getLabel() {
        return this.label;
    }
}
