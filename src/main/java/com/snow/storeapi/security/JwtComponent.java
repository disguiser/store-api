package com.snow.storeapi.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snow.storeapi.entity.JwtCheckResult;
import com.snow.storeapi.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * jwt加密和解密的工具类
 */
@Component
public class JwtComponent {
	public JwtComponent(
			ObjectMapper objectMapper,
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.ttl}") Long ttl
	) {
		this.objectMapper = objectMapper;
		this.secret = secret;
		this.ttl = ttl;
	}
	private final ObjectMapper objectMapper;

	private final String secret;

	private final Long ttl;

	private final ObjectMapper objectMapper2 = new ObjectMapper();
	/**
	 * 签发JWT
	 * @param subject 可以是JSON数据 尽可能少
	 * @param ttlMillis
	 * @return  String
	 *
	 */
	public static String createJWT(String subject, long ttlMillis, String secret) {
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		Key key = generalKey(secret);
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

	public static Key generalKey(String secret) {
		byte[] encodedKey = secret.getBytes();
        Key key = Keys.hmacShaKeyFor(encodedKey);
        return key;
    }
	
	/**
	 * 
	 * 解析JWT字符串
	 * @param token
	 * @return Claims
	 */
	public static Claims getAllClaimsFromToken(String token, String secret) {
        Key key = generalKey(secret);
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public static <T> T getClaimFromToken(String token, String secret, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token, secret);
		return claimsResolver.apply(claims);
	}


	/**
	 * 从请求中拿出token并解析出用户信息
	 * @param request
	 * @return
	 */
	public User getSub(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		try {
			String subString = getClaimFromToken(token.substring(7), secret, Claims::getSubject);
			return objectMapper.readValue(subString, User.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 验证JWT
	 * @param token
	 * @return
	 */
	public JwtCheckResult validateGetSubject(String token) {
		JwtCheckResult jwtCheckResult = new JwtCheckResult();
		jwtCheckResult.setSuccess(false);
		try {
			Claims claims = getAllClaimsFromToken(token, secret);
			jwtCheckResult.setSuccess(true);
			jwtCheckResult.setUser(objectMapper.readValue(claims.getSubject(), User.class));
		} catch (SignatureException e) {
			jwtCheckResult.setErrMsg("Invalid JWT signature.");
		} catch (MalformedJwtException e) {
			jwtCheckResult.setErrMsg("Invalid JWT token.");
		} catch (ExpiredJwtException e) {
			jwtCheckResult.setErrMsg("Expired JWT token.");
		} catch (UnsupportedJwtException e) {
			jwtCheckResult.setErrMsg("Unsupported JWT token.");
		} catch (IllegalArgumentException e) {
			jwtCheckResult.setErrMsg("JWT token compact of handler are invalid.");
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		return jwtCheckResult;
	}

	public String generateToken(Object subject) {
		try {
			return createJWT(objectMapper.writeValueAsString(subject), ttl, secret);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getTestToken() throws JsonProcessingException {
		Map<String, Object> sub = new HashMap<>();
		sub.put("id", "24");
		sub.put("userName", "test");
//		sub.put("deptId", "");
		var objectMapper = new ObjectMapper();
		return createJWT(objectMapper.writeValueAsString(sub), ttl, secret);
	}
}
