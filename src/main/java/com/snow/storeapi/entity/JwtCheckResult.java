package com.snow.storeapi.entity;

import lombok.Data;

/**
 * 验证信息
 */
@Data
public class JwtCheckResult {
	private String errMsg;

	private boolean success;

	private User user;
}
