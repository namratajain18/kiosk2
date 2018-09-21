package com.cusbee.kiosk.entity;

import com.cusbee.kiosk.entity.AbstractEntity;
import com.cusbee.kiosk.entity.MenuItems;
import com.cusbee.kiosk.entity.Menus;
import com.cusbee.kiosk.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "categories")
public class Categories extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 195229190496959564L;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "imgURL")
	private String url;
	@Column
	private Integer displayPosition;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Set<MenuItems> menuItems;
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "menue_id")
	@JsonIgnore
	private Menus menus;
	@Enumerated(EnumType.STRING)
	@Column(name = "category_type")
	private CategoryType categoryType;
	@ManyToOne
	@JoinColumn(name = "parent_id", referencedColumnName = "id")
	private Categories parentCategory;
	@OrderColumn(name = "position")
	@OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	private List<Categories> subCategories = new ArrayList();
	@Transient
	private Integer tax;
	@Column(name = "category_belong")
	private String categoryBelong;

	public String getCategoryBelong() {
		return this.categoryBelong;
	}

	public void setCategoryBelong(String categoryBelong) {
		this.categoryBelong = categoryBelong;
	}

	public Integer getDisplayPosition() {
		return this.displayPosition;
	}

	public void setDisplayPosition(Integer displayPosition) {
		this.displayPosition = displayPosition;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<MenuItems> getMenuItems() {
		return this.menuItems;
	}

	public void setMenuItems(Set<MenuItems> menuItems) {
		this.menuItems = menuItems;
	}

	public Menus getMenus() {
		return this.menus;
	}

	public void setMenus(Menus menus) {
		this.menus = menus;
	}

	public CategoryType getCategoryType() {
		return this.categoryType == null ? CategoryType.STANDARD : this.categoryType;
	}

	public void setCategoryType(CategoryType categoryType) {
		this.categoryType = categoryType;
	}

	public Categories getParentCategory() {
		return this.parentCategory;
	}

	public void setParentCategory(Categories parentCategory) {
		this.parentCategory = parentCategory;
	}

	public List<Categories> getSubcategories() {
		return this.subCategories;
	}

	public void setSubcategories(List<Categories> subCategories) {
		this.subCategories = subCategories;
	}

	public Integer getTax() {
		return this.tax;
	}

	public void setTax(Integer tax) {
		this.tax = tax;
	}

	public String toString() {
		return "Categories{name=\'" + this.name + '\'' + ", description=\'" + this.description + '\'' + ", url=\'"
				+ this.url + '\'' + ", displayPosition=" + this.displayPosition + ", menuItems=" + this.menuItems
				+ ", menus=" + this.menus + ", categoryType=" + this.categoryType + ", parentCategory="
				+ this.parentCategory + ", subCategories=" + this.subCategories + ", tax=" + this.tax + '}';
	}

	public boolean equals(Object obj) {
		return !(obj instanceof Categories)
				? false
				: super.equals(obj) && Objects.equals(((Categories) obj).name, this.name);
	}

	public int hashCode() {
		return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), this.name});
	}
}