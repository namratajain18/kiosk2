package com.cusbee.kiosk.enums;

public enum PackageType {
	PICK_UP("Pick-up"), DINE_IN("Dine-in"), DELIVERY("Delivery");

	private String description;

	private PackageType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}