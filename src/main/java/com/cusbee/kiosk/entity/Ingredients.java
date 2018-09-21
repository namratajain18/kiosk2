package com.cusbee.kiosk.entity;

import com.cusbee.kiosk.enums.IngredientType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ahorbat on 11.02.17.
 */
@Entity
@Table(name = "ingredients")
public class Ingredients extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -2408269025056766216L;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "remove")
    private boolean remove = false;

    @Column(name = "price")
    private Double price;

    @Column(name = "type")
    private IngredientType ingredientType = IngredientType.Ingredient;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "menue_item_id")
    @JsonIgnore
    private MenuItems menuItems;

    public IngredientType getIngredientType() {
        return ingredientType;
    }

    public void setIngredientType(IngredientType ingredientType) {
        this.ingredientType = ingredientType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public MenuItems getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(MenuItems menuItems) {
        this.menuItems = menuItems;
    }
}
