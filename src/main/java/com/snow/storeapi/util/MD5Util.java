package com.snow.storeapi.util;

import java.security.MessageDigest;

/**
 * MD5加密,用于登录密码加密
 */
public class MD5Util {
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private Object salt;
    private String algorithm;

    public MD5Util(Object salt, String algorithm) {
        this.salt = salt;
        this.algorithm = algorithm;
    }

    public String encode(String rawPass) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            //加密后的字符串 
            result = byteArrayToHexString(md.digest(mergePasswordAndSalt(rawPass).getBytes("utf-8")));
        } catch (Exception ex) {
        }
        return result;
    }

    public boolean isPasswordValid(String encPass, String rawPass) {
        String pass1 = "" + encPass;
        String pass2 = encode(rawPass);

        return pass1.equals(pass2);
    }

    private String mergePasswordAndSalt(String password) {
        if (password == null) {
            password = "";
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }

    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static void main(String[] args) {
        String salt = "helloworld";
        MD5Util encoderMd5 = new MD5Util(salt, "MD5");
        String encode = encoderMd5.encode("12345");
        System.out.println(encode);
        boolean passwordValid = encoderMd5.isPasswordValid("c6d5343ac6321a87f16259de57192022", "12345");
        System.out.println(passwordValid);

        MD5Util encoderSha = new MD5Util(salt, "SHA");
        String pass2 = encoderSha.encode("123root");
        System.out.println(pass2);
        boolean passwordValid2 = encoderSha.isPasswordValid("8c56ce555fd08da90674c5576ef740c8702f0365", "123root");
        System.out.println(passwordValid2);
    }
}
