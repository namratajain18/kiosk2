package com.cusbee.kiosk.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cusbee.kiosk.enums.CookType;
import com.cusbee.kiosk.enums.KitchenStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by ahorbat on 12.02.17.
 */
@Entity
@Table(name = "order_details")
public class OrderDetails extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -6057483070918600584L;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "title")
    private String title;

    @Column(name = "count")
    private Integer count;

    @Column(name = "cook_type")
    private CookType cookType;

    @Column(name = "extras")
    private String extras;

    @Column(name = "modifiers")
    private String modifiers;

    @Column(name = "item_status")
    private KitchenStatus itemStatus = KitchenStatus.IN_PROGRESS;

    @Column(name = "extra_ids")
    private String extraIds;

    @Column(name = "temperature")
    private String temperature;
    
    @Column(name = "serve")
    private String serve;
    
    @Column(name = "size")
    private String size;
    
    @Column(name = "instructions")
    private String instructions;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Orders orders;


	public String getServe() {
		return serve;
	}

	public void setServe(String serve) {
		this.serve = serve;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getModifiers() {
        return modifiers;
    }

    public void setModifiers(String modifiers) {
        this.modifiers = modifiers;
    }


	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public CookType getCookType() {
        return cookType;
    }

    public void setCookType(CookType cookType) {
        this.cookType = cookType;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public KitchenStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(KitchenStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getExtraIds() {
        return extraIds;
    }

    public void setExtraIds(String extraIds) {
        this.extraIds = extraIds;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
    
}
