package com.cusbee.kiosk.enums;

import com.cusbee.kiosk.bean.dto.IngredientDTO;

public enum CategoryType {
	STANDARD("NO ", " WITH "), CUSTOM("WITH ", " "), EXTRA("WITH EXTRA ", " ");

	private final String stopWord;
	private final String begin;

	private CategoryType(String stopWord, String begin) {
		this.stopWord = stopWord;
		this.begin = begin;
	}

	public String getStopWord() {
		return this.stopWord;
	}

	public String getBegin() {
		return this.begin;
	}

	public boolean isAppendable(IngredientDTO dto) {
		return true;
	}
}