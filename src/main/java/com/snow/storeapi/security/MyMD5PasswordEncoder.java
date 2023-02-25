package com.snow.storeapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 凭证匹配器，用于做认证流程的凭证校验使用的类型
 * 其中有2个核心方法
 * 1. encode - 把明文密码，加密成密文密码
 * 2. matches - 校验明文和密文是否匹配
 * */
@Component
public class MyMD5PasswordEncoder implements PasswordEncoder {
    private final String SALT;
    public MyMD5PasswordEncoder(@Value("${SALT}") String SALT) {
        this.SALT = SALT;
    }
    /**
     * 加密
     * @param charSequence  明文字符串
     * @return
     */
    @Override
    public String encode(CharSequence charSequence) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return toHexString(digest.digest((charSequence.toString() + SALT).getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 密码校验
     * @param charSequence 明文，页面收集密码
     * @param s 密文 ，数据库中存放密码
     * @return
     */
    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(encode(charSequence));
    }

    /**
     * @param tmp 转16进制字节数组
     * @return 饭回16进制字符串
     */
    private String toHexString(byte [] tmp){
        StringBuilder builder = new StringBuilder();
        for (byte b :tmp){
            String s = Integer.toHexString(b & 0xFF);
            if (s.length()==1){
                builder.append("0");
            }
            builder.append(s);
        }

        return builder.toString();

    }
}
