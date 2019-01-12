package com.pab.framework.crawlerengine.enums;

public enum ActionTargetParamNameEnum {

    SAVE_TYPE("SAVE_TYPE");

    private String label;

    ActionTargetParamNameEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
