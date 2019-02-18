package com.pab.framework.crawlerengine.enums;

public enum ActionTargetParamTypeEnum {

    MILESTONE("MILESTONE"),
	FILTER("FILTER");

    private String label;

    ActionTargetParamTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
