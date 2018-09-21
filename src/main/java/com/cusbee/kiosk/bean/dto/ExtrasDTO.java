package com.cusbee.kiosk.bean.dto;

import java.io.Serializable;

/**
 * Created by ahorbat on 10.02.18.
 */
public class ExtrasDTO implements Serializable {
    private Long id;
    private String name;
    private double price;
    private boolean remove = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    
    

    public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
    public String toString() {
        return "ExtrasDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                 ", price=" + price +
                ", remove=" + remove +
                '}';
    }
}
