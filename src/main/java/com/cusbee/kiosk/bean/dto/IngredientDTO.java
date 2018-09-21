package com.cusbee.kiosk.bean.dto;

import java.io.Serializable;

/**
 * Created by ahorbat on 12.02.17.
 */
public class IngredientDTO implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
    private boolean remove = false;
    private double price;

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

    
    
    public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
    public String toString() {
        return "IngredientDTO{" +
            "name='" + name + '\'' +
            ", price=" + price +
            ", remove=" + remove +
            '}';
    }
}
