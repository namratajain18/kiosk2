package com.cusbee.kiosk.enums;

public enum Ban {
	BANNED("User is Banned"), ENABLED("User not banned.");

	private String description;

	private Ban(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}