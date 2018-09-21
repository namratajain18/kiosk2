package com.cusbee.kiosk.bean.dto;

import java.io.Serializable;

public class ItemDimentionsDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String size;
	private String serve;
	private Double price;

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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

    @Override
    public String toString() {
        return "ItemDimentionsDTO{" +
            "size='" + size + '\'' +
            "serve='" + serve + '\'' +
            ", price=" + price +
            '}';
    }

}
