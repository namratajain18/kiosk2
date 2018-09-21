package com.cusbee.kiosk.enums;

public enum Gender {
	UNKNOWN("Unknown"), MALE("Male"), FEMALE("Female");

	private String description;

	private Gender(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}