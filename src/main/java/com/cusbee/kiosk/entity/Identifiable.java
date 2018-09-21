package com.cusbee.kiosk.entity;

import java.io.Serializable;

public interface Identifiable<ID extends Serializable> {
	ID getId();
}