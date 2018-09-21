package com.cusbee.kiosk.enums;

public enum Role {
	ROLE_ADMIN("Admin"), ROLE_EMPLOYEE("Employee"), ROLE_CUSTOMER("Customer"), ROLE_SALAD("Trial User"), ROLE_SANDWICH(
			"Full User"), ROLE_BURGER("Expired User");

	private String description;

	private Role(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}