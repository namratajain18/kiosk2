package com.cusbee.kiosk.bean;

import com.cusbee.kiosk.entity.OrderDetails;
import com.cusbee.kiosk.enums.KitchenStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahorbat on 08.03.17.
 */
public class OrderDetailsBean {

    private Long id;

    private String title;

    private Integer count;

    private KitchenStatus kitchenStatus;

    private Long menuItemId;
    
    private String modifiers;
    
    private String extras;
    
    private String temperature;
    private String instructions;

    private String size;
    private String serve;
    
    
    
    
    public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getServe() {
		return serve;
	}


	public void setServe(String serve) {
		this.serve = serve;
	}


	public Long getMenuItemId() {
        return menuItemId;
    }

    
    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public KitchenStatus getKitchenStatus() {
        return kitchenStatus;
    }

    public void setKitchenStatus(KitchenStatus kitchenStatus) {
        this.kitchenStatus = kitchenStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    
    

    public String getModifiers() {
		return modifiers;
	}


	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}


	public String getExtras() {
		return extras;
	}


	public void setExtras(String extras) {
		this.extras = extras;
	}


	public String getTemperature() {
		return temperature;
	}


	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}


	public String getInstructions() {
		return instructions;
	}


	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}


	public static OrderDetailsBean toOrderDetailsBean(OrderDetails orderDetails) {
        OrderDetailsBean bean = new OrderDetailsBean();
        bean.setId(orderDetails.getId());
        bean.setCount(orderDetails.getCount());
        bean.setTitle(orderDetails.getTitle());
        bean.setKitchenStatus(orderDetails.getItemStatus());
        bean.setMenuItemId(orderDetails.getItemId());
        bean.setExtras(orderDetails.getExtras());
        bean.setModifiers(orderDetails.getModifiers());
        bean.setTemperature(orderDetails.getTemperature());
        bean.setInstructions(orderDetails.getInstructions());
        bean.setServe(orderDetails.getServe());
        bean.setSize(orderDetails.getSize());
        return bean;
    }

    public static List<OrderDetailsBean> toBeanList(List<OrderDetails> orderDetailsList) {
        List<OrderDetailsBean> orderBeanList = new ArrayList<>();
        for (OrderDetails orders : orderDetailsList) {
            orderBeanList.add(OrderDetailsBean.toOrderDetailsBean(orders));
        }
        return orderBeanList;
    }
}
