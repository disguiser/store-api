package com.snow.storeapi.util;

public class StringUtil {
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        } else {
            return false;
        }
    }
}
