package com.snow.storeapi.util;

import com.alibaba.fastjson.JSON;
import com.snow.storeapi.constant.SystemConstant;
import com.snow.storeapi.entity.CheckResult;
import com.snow.storeapi.entity.User;
import io.jsonwebtoken.*;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * jwt加密和解密的工具类
 */
public class JwtUtils {
	/**
	 * 签发JWT
	 * @param id
	 * @param subject 可以是JSON数据 尽可能少
	 * @param ttlMillis
	 * @return  String
	 *
	 */
	public static String createJWT(String id, String subject, long ttlMillis) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		SecretKey secretKey = generalKey();
		JwtBuilder builder = Jwts.builder()
				.setId(id)
				.setSubject(subject)   // 主题
				.setIssuer("瑞雪")     // 签发者
				.setIssuedAt(now)      // 签发时间
				.signWith(signatureAlgorithm, secretKey); // 签名算法以及密匙
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
		} catch (SignatureException e) {
			checkResult.setErrCode(SystemConstant.JWT_ERRCODE_FAIL);
			checkResult.setSuccess(false);
		} catch (Exception e) {
			checkResult.setErrCode(SystemConstant.JWT_ERRCODE_FAIL);
			checkResult.setSuccess(false);
		}
		return checkResult;
	}
	public static SecretKey generalKey() {
		byte[] encodedKey = Base64.decode(SystemConstant.JWT_SECERT);
	    SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
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
		SecretKey secretKey = generalKey();
		return Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(jwt)
			.getBody();
	}

	/**
	 * 从请求中拿出token并解析出用户信息
	 * @param request
	 * @return
	 */
	public static User getSub(HttpServletRequest request) {
		String token = request.getHeader("token");
		try {
			String subString = parseJWT(token).getSubject();
			return JSON.parseObject(subString, User.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		//小明失效 10s
//		String sc = createJWT("1","小明", 3000);
//		System.out.println(sc);
//		System.out.println(validateJWT(sc).getErrCode());
//		System.out.println(validateJWT(sc).getClaims().getId());
//		//Thread.sleep(3000);
//		System.out.println(validateJWT(sc).getClaims());
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoie1wib3BOYW1lXCI6XCLns7vnu5_nrqHnkIblkZhcIixcImRlcGFydElkXCI6XCI4MDFcIixcIm9wQ29kZVwiOlwiODg4XCJ9IiwiaXNzIjoi55Ge6ZuqIiwiaWF0IjoxNTM2OTEyNjM1LCJleHAiOjE1Mzc1MTc0MzV9.s6-oVwkrswfa9xR055io28YcqS5JeqlfSJlmyb3aRZM";
		try {
			System.out.println(parseJWT(token).getSubject());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
