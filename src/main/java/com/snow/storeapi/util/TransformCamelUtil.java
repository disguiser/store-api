package com.snow.storeapi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformCamelUtil {

    /**
     * 下划线转驼峰
     * @param s
     * @return
     */
    public static String camel(String s) {
        StringBuffer str = new StringBuffer(s);
        //利用正则删除下划线，把下划线后一位改成大写
        Pattern pattern = Pattern.compile("_(\\w)");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if(matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        }else {
            return sb.toString();
        }
        return camel(sb.toString());
    }


    /**
     * 驼峰转下划线
     * @param s
     * @return
     */
    public static String underline(String s) {
        StringBuffer str = new StringBuffer(s);
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if(matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb,"_"+matcher.group(0).toLowerCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        }else {
            return sb.toString();
        }
        return underline(sb.toString());
    }
}
