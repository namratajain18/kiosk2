package com.cusbee.kiosk.bean.dto;

import com.cusbee.kiosk.enums.CookType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by btomar on 06.05.18.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenueItemDTO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private String name;
    private int count = 0;
    private double price;
    private int quantity = 1;
    private CookType cookType;
    private String instructions;
    private List<IngredientDTO> ingredients;
    private List<ExtrasDTO> extras;
    private List<MenueItemDTO> subMenuItems;
    private List<ModifiersDTO> modifiers;
    private String temperature;
    private String size;
    private String serve;
    

    public String getServe() {
		return serve;
	}

	public void setServe(String serve) {
		this.serve = serve;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String currentPrice) {
		this.size = currentPrice;
	}

	public List<ExtrasDTO> getExtras() {
        return extras;
    }

    public void setExtras(List<ExtrasDTO> extras) {
        this.extras = extras;
    }

    public List<ModifiersDTO> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<ModifiersDTO> modifiers) {
		this.modifiers = modifiers;
	}

	public CookType getCookType() {
        return cookType;
    }

    public void setCookType(CookType cookType) {
        this.cookType = cookType;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(final String instructions) {
        this.instructions = instructions;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        if (count == null) {
            this.count = 0;
        } else {
            this.count = count;
        }
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<IngredientDTO> getIngredients() {
        return ingredients == null ? Lists.newArrayList() : ingredients;
    }

    public void setIngredients(List<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }
    
    
	public List<MenueItemDTO> getSubMenuItems() {
        return subMenuItems == null ? new ArrayList<>() : subMenuItems;
    }

    public void setSubMenuItems(List<MenueItemDTO> subMenuItems) {
        this.subMenuItems = subMenuItems;
    }
    
    public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	@Override
    public String toString() {
        return "MenueItemDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", count=" + count +
             ", size='" + size + '\'' +
             ", serve='" + serve + '\'' +
            ", price=" + price +
            ", quantity=" + quantity +
            ", cookType=" + cookType +
            ", instructions='" + instructions + '\'' +
            ", ingredients=" + ingredients +
            ", extras=" + extras +
             ", modifiers=" + modifiers +
            ", subMenuItems=" + subMenuItems +
            ", temperature='" + temperature + '\'' +
            '}';
    }
}
