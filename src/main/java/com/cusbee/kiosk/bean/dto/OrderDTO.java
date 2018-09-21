package com.cusbee.kiosk.bean.dto;

import com.cusbee.kiosk.enums.PackageType;
import com.cusbee.kiosk.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by ahorbat on 12.02.17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO implements Serializable {
    private String name;
    private String phone;
    private String email;
    private Double orderPriceSubTotal = 0.0;
    private Double orderPriceTip = 0.0;
    private Double orderPriceTax = 0.0;
    private Double orderDeliveryPrice = 0.0;
    private Double orderPrice = 0.0;
    private PaymentStatus paymentStatus;
    private String deliveryAddress;
    private PackageType packageType = PackageType.DINE_IN;
    private Long paymentId;
    private String source;
    private String address;
    private List<MenueItemDTO> menuItems;
    private String paymentData;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;
    private int table;
    
    Long orderId;
    private String restaurant;
    private String logo;
    private String pickUp;
    private String warnMsg;
    private String location;
    
    
    private String merchId;
    private String transDate;
    private String approvalCode;
    private String nameOnCard;
    private String cardNumber;
	private String pinStatement;
    private String appId;
    private String appLabel;
    private String terminalId;
    
    
    public String getMerchId() {
		return merchId;
	}

	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getApprovalCode() {
		return approvalCode;
	}

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getPinStatement() {
		return pinStatement;
	}

	public void setPinStatement(String pinStatement) {
		this.pinStatement = pinStatement;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppLabel() {
		return appLabel;
	}

	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getPaymentData() {
		return paymentData;
	}

	public void setPaymentData(String paymentData) {
		this.paymentData = paymentData;
	}

	public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getOrderPriceTax() {
        return orderPriceTax;
    }

    public void setOrderPriceTax(Double orderPriceTax) {
        this.orderPriceTax = orderPriceTax;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public List<MenueItemDTO> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenueItemDTO> menuItems) {
        this.menuItems = menuItems;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getOrderPriceSubTotal() {
        return orderPriceSubTotal;
    }

    public void setOrderPriceSubTotal(final Double orderPriceSubTotal) {
        this.orderPriceSubTotal = orderPriceSubTotal;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Double getOrderPriceTip() {
        return orderPriceTip;
    }

    public void setOrderPriceTip(final Double orderPriceTip) {
        this.orderPriceTip = orderPriceTip;
    }

    public Double getOrderDeliveryPrice() {
        return orderDeliveryPrice;
    }

    public void setOrderDeliveryPrice(final Double orderDeliveryPrice) {
        this.orderDeliveryPrice = orderDeliveryPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
            "name='" + name + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", orderPriceSubTotal=" + orderPriceSubTotal +
            ", orderPriceTip=" + orderPriceTip +
            ", orderPriceTax=" + orderPriceTax +
            ", orderDeliveryPrice=" + orderDeliveryPrice +
            ", orderPrice=" + orderPrice +
            ", paymentStatus=" + paymentStatus +
            ", packageType=" + packageType +
            ", paymentId=" + paymentId +
            ", source='" + source + '\'' +
            ", address='" + address + '\'' +
            ", menuItems=" + menuItems +
            ", orderDate=" + orderDate +
            ", table=" + table +
            '}';
    }

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getPickUp() {
		return pickUp;
	}

	public void setPickUp(String pickUp) {
		this.pickUp = pickUp;
	}

	public String getWarnMsg() {
		return warnMsg;
	}

	public void setWarnMsg(String warnMsg) {
		this.warnMsg = warnMsg;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
    
}
