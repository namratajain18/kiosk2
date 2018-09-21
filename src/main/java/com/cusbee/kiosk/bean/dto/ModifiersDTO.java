package com.cusbee.kiosk.bean.dto;

import java.io.Serializable;

public class ModifiersDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private boolean remove = false;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRemove() {
		return this.remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	public String toString() {
		return "ModifiersDTO{id=" + this.id + ", name=\'" + this.name + '\'' + ", remove=" + this.remove + '}';
	}
}