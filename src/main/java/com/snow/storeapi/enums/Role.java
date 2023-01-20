package com.snow.storeapi.enums;

public enum Role {
    ADMIN("Admin"),
    BOSS("Boss"),
    WAITER("Waiter");

    private String value;
    Role(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
