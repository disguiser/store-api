package com.snow.storeapi;

import com.snow.storeapi.constant.SystemConstant;
import org.bouncycastle.util.encoders.Base64;

import java.util.Arrays;
import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        byte[] encodedKey = Base64.decode(SystemConstant.JWT_SECERT);
        byte[] a = SystemConstant.JWT_SECERT.getBytes();
        System.out.println(encodedKey);
        System.out.println(a);
    }
}
