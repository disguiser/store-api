package com.snow.storeapi;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestConstant {
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    public static final Integer CUSTOMER_ID = 1;

    public static final String CUSTOMER_CREATE_NAME = "王庆云";

    public static final String CUSTOMER_CREATE_MOBILE = "15163927336";

    public static final String[] CUSTOMER_CREATE_ADDRESS = {"37", "3713", "371323", "371323001"};
//    public static final List<String> CUSTOMER_CREATE_ADDRESS = Stream.of("37", "3713", "371323", "371323001").collect(Collectors.toList());
//    public static final List<String> CUSTOMER_CREATE_ADDRESS = Arrays.asList("37", "3713", "371323", "371323001");

    public static final String CUSTOMER_CREATE_ADDRESS_DETAIL = "双城路18号";

    public static final Integer CUSTOMER_CREATE_DEBT = 1000;

    public static final Integer BILL_CREATE_AMOUNT = -100;

    public static final Integer BILL_UPDATE_AMOUNT = -200;

    public static final String GOODS_CREATE_PRE_SKU = "YL903";

    public static final String GOODS_CREATE_NAME = "羽绒服";

    public static final Integer GOODS_CREATE_SIZE_GROUP = 15;
    public static final String GOODS_CREATE_IMG_URL = "https://shanxi-1256183582.cos.ap-beijing.myqcloud.com/goods/09e11f7130c94207891d7fecb2825f34.png";

    public static final Integer GOODS_CREATE_SALE_PRICE = 1398;

    public static final Integer GOODS_CREATE_COST_PRICE = 500;
}
