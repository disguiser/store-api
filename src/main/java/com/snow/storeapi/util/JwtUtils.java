package com.snow.storeapi.util;

import com.alibaba.fastjson.JSON;
import com.snow.storeapi.constant.SystemConstant;
import com.snow.storeapi.entity.CheckResult;
import com.snow.storeapi.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

/**
 * jwt加密和解密的工具类
 */
public class JwtUtils {
	/**
	 * 签发JWT
	 * @param subject 可以是JSON数据 尽可能少
	 * @param ttlMillis
	 * @return  String
	 *
	 */
	public static String createJWT(String subject, long ttlMillis) {
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		Key key = generalKey();
//		byte[] secretKey = SystemConstant.JWT_SECERT.getBytes("UTF-8");
		JwtBuilder builder = Jwts.builder()
				.setSubject(subject)   // 主题
				.setIssuer("周明帅")     // 签发者
				.setIssuedAt(now)      // 签发时间
				.signWith(key, SignatureAlgorithm.HS256); // 签名算法以及密匙
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date expDate = new Date(expMillis);
			builder.setExpiration(expDate); // 过期时间
		}
		return builder.compact();
	}
	/**
	 * 验证JWT
	 * @param jwtStr
	 * @return
	 */
	public static CheckResult validateJWT(String jwtStr) {
		CheckResult checkResult = new CheckResult();
		Claims claims = null;
		try {
			claims = parseJWT(jwtStr);
			checkResult.setSuccess(true);
			checkResult.setClaims(claims);
		} catch (ExpiredJwtException e) {
			checkResult.setErrCode(SystemConstant.JWT_ERRCODE_EXPIRE);
			checkResult.setSuccess(false);
		} catch (JwtException e) {
			checkResult.setErrCode(SystemConstant.JWT_ERRCODE_FAIL);
			checkResult.setSuccess(false);
		} catch (Exception e) {
			checkResult.setErrCode(SystemConstant.JWT_ERRCODE_FAIL);
			checkResult.setSuccess(false);
		}
		return checkResult;
	}
	public static Key generalKey() {
		byte[] encodedKey = SystemConstant.JWT_SECERT.getBytes();
//	    Key key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        Key key = Keys.hmacShaKeyFor(encodedKey);
        return key;
    }
	
	/**
	 * 
	 * 解析JWT字符串
	 * @param jwt
	 * @return
	 * @throws Exception
	 */
	public static Claims parseJWT(String jwt) throws Exception {
        Key key = generalKey();
		return Jwts.parser()
			.setSigningKey(key)
			.parseClaimsJws(jwt)
			.getBody();
	}

	/**
	 * 从请求中拿出token并解析出用户信息
	 * @param request
	 * @return
	 */
	public static User getSub(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		try {
			String subString = parseJWT(token.substring(7)).getSubject();
			return JSON.parseObject(subString, User.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		//小明失效 10s
		String sc = createJWT("{'a':'1'}", 30000);
        System.out.println(sc);
//        Thread.sleep(3000);
		System.out.println(validateJWT(sc).getErrCode());
//		System.out.println(validateJWT(sc).getClaims().getId());
		System.out.println(validateJWT(sc).getClaims());
//		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7J2EnOicxJ30iLCJpc3MiOiLnkZ7pm6oiLCJpYXQiOjE1NzIyNDEyMzgsImV4cCI6MTU3MjI0MTI0MX0.IaYWAwKh5ONteHxHnMiLh5cWuiHKIlPcuT-SF4_LYIe4_1dRAZ4G4VXX-L-9Y9TDJ2c7-qqArji7emIiAppylw";
		try {
			System.out.println(parseJWT(sc).getSubject());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
