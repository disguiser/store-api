package com.snow.storeapi.entity;

import lombok.Data;

import java.util.List;

@Data
public class MyPage {
    private long total;

    private long page;

    private long size;

    private List<?> items;
}
