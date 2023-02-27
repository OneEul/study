package com.product.test;

import lombok.Getter;

@Getter
public enum UserRole {
	USER("ROLE_USER");

	UserRole(String value) {
		this.value = value;
	}
	
	private String value;
}
