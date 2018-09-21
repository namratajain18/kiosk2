package com.cusbee.kiosk.entity;

import com.cusbee.kiosk.entity.AbstractEntity;
import com.cusbee.kiosk.entity.MenuItems;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "item_dimentions")
public class ItemDimentions extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = -5659095962504920714L;
	@Column(name = "size", nullable = false)
	private String size;
	@Column(name = "serve")
	private String serve;
	@Column(name = "price")
	private Double price;
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "menue_item_id", nullable = false, columnDefinition = "int")
	@JsonIgnore
	private MenuItems menuItems;

	public String getServe() {
		return this.serve;
	}

	public void setServe(String serve) {
		this.serve = serve;
	}

	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public MenuItems getMenuItems() {
		return this.menuItems;
	}

	public void setMenuItems(MenuItems menuItems) {
		this.menuItems = menuItems;
	}
}