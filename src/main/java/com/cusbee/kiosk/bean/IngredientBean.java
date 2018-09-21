package com.cusbee.kiosk.bean;

import java.io.Serializable;

import com.cusbee.kiosk.entity.Ingredients;

/**
 * Created by ahorbat on 11.02.17.
 */
public class IngredientBean implements Serializable {

    private Long id;

    private String name;

    private Double price;

    private boolean remove = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public static IngredientBean toBean(Ingredients ingredients) {
        IngredientBean bean = new IngredientBean();
        bean.setId(ingredients.getId());
        bean.setName(ingredients.getName());
        bean.setRemove(ingredients.isRemove());
        bean.setPrice(ingredients.getPrice());
        return bean;
    }


}
