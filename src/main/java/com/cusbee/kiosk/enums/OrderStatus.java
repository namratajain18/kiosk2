package com.cusbee.kiosk.enums;

public enum OrderStatus {
	IN_PROGRESS("Preparing"), DONE("Done"), CANCEL("Cancelled");

	private String description;

	private OrderStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}