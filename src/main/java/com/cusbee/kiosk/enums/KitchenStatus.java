package com.cusbee.kiosk.enums;

public enum KitchenStatus {
	IN_PROGRESS("In progress"), COMPLETE("Complete");

	private String description;

	private KitchenStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}