package com.cusbee.kiosk.entity;

import com.cusbee.kiosk.entity.AbstractEntity;
import com.cusbee.kiosk.entity.Categories;
import com.cusbee.kiosk.entity.Ingredients;
import com.cusbee.kiosk.entity.ItemDimentions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "menue_items")
public class MenuItems extends AbstractEntity implements Serializable {
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "description", nullable = false)
	private String description;
	@Column(name = "url")
	private String url;
	@Column(name = "price")
	private BigDecimal price;
	@Column(name = "top_seller")
	private Boolean topSeller = Boolean.valueOf(false);
	@Column(name = "specialities")
	private Boolean specialities = Boolean.valueOf(false);
	@Column(name = "cookRules")
	private Boolean cookRules = Boolean.valueOf(false);
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "menuItems", cascade = {CascadeType.ALL})
	@JsonIgnore
	private List<Ingredients> ingredients;
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "categories")
	@JsonIgnore
	private Categories category;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "menuItems", cascade = {CascadeType.ALL})
	@JsonIgnore
	private List<ItemDimentions> itemDimentions;
	@Column(name = "size")
	private String size;
	@Column(name = "serve")
	private String serve;

	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getServe() {
		return this.serve;
	}

	public void setServe(String serve) {
		this.serve = serve;
	}

	public List<ItemDimentions> getItemDimentions() {
		return this.itemDimentions;
	}

	public void setItemDimentions(List<ItemDimentions> itemDimentions) {
		this.itemDimentions = itemDimentions;
	}

	public Boolean isCookRules() {
		return this.cookRules;
	}

	public void setCookRules(Boolean cookRules) {
		this.cookRules = cookRules;
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

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isTopSeller() {
		return this.topSeller.booleanValue();
	}

	public void setTopSeller(boolean topSeller) {
		this.topSeller = Boolean.valueOf(topSeller);
	}

	public boolean isSpecialities() {
		return this.specialities.booleanValue();
	}

	public void setSpecialities(boolean specialities) {
		this.specialities = Boolean.valueOf(specialities);
	}

	public List<Ingredients> getIngredients() {
		return this.ingredients;
	}

	public void setIngredients(List<Ingredients> ingredients) {
		this.ingredients = ingredients;
	}

	public Categories getCategory() {
		return this.category;
	}

	public void setCategory(Categories category) {
		this.category = category;
	}

	public boolean equals(Object obj) {
		return !(obj instanceof MenuItems)
				? false
				: super.equals(obj) && Objects.equals(((MenuItems) obj).name, this.name);
	}

	public int hashCode() {
		return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), this.name});
	}
}