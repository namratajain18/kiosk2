package com.cusbee.kiosk.enums;

public enum IngredientType {
	Ingredient("Ingredient"), Extra("Extra");

	private String description;

	private IngredientType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
}