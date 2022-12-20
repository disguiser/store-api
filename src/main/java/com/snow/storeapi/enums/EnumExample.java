package com.snow.storeapi.enums;

public enum EnumExample {
    结清(1), 欠款(0);

    private Integer value;

    private EnumExample(Integer value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
