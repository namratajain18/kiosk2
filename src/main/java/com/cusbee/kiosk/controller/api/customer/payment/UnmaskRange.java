package com.cusbee.kiosk.controller.api.customer.payment;

public class UnmaskRange {
	public static final int BEGINNINGTYPE = 0;
	public static final int ENDTYPE = 1;
	private final int positionType;
	private final int length;

	public UnmaskRange(int startPosition, int length) {
		this.positionType = startPosition;
		this.length = length;
	}

	public int getPositionType() {
		return this.positionType;
	}

	public int getLength() {
		return this.length;
	}
}