package com.cusbee.kiosk.enums;

public enum PaymentStatus {
	NOT_PAYED("Payment not made yet"), PAYED_BY_CARD("Payed by credit card"), PAYED_BY_CACHE("Payed by cache"), FAILED(
			"Failed to pay"), REFUND("Payment refund"), REFUND_FAILED("Payment refund failed");

	private String description;

	private PaymentStatus(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}