package com.cusbee.kiosk.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.cusbee.kiosk.enums.OrderStatus;
import com.cusbee.kiosk.enums.PackageType;
import com.cusbee.kiosk.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by ahorbat on 12.02.17.
 */
@Entity
@Table(name = "orders")
public class Orders extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 9193949143694800294L;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.NOT_PAYED;

    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.CANCEL;

    @Column(name = "order_price", nullable = false)
    private BigDecimal orderPrice = BigDecimal.ZERO;

    @Column(name = "order_price_tax")
    private BigDecimal orderPriceTax = BigDecimal.ZERO;

    @Column(name = "package", nullable = false)
    private PackageType packageType = PackageType.PICK_UP;

    @Column(name = "email")
    private String email;

    @Column(name = "source")
    private String Source;

    @Column(name = "address")
    private String address;

    @Column(name = "count_down")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date countDown = new Date();

    @Column(name = "order_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date orderDate = new Date();

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "current_table", nullable = false)
    private int table = 1;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orders", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderDetails> orderDetailsList;

    @Column(name = "client_order_id", nullable = false)
    private Integer orderId;
    
    @Column(name = "order_details_blob")
    private Blob orderDetailsBlob;
    
    @Column(name = "paymentData")
    private String paymentData;
    
    @Column(name = "discount", nullable = false)
    private BigDecimal discount = BigDecimal.ZERO;
   
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public String getPaymentData() {
		return paymentData;
	}
	public void setPaymentData(String paymentData) {
		this.paymentData = paymentData;
	}
	public Blob getOrderDetailsBlob() {
		return orderDetailsBlob;
	}
	public void setOrderDetailsBlob(Blob orderDetailsBlob) {
		this.orderDetailsBlob = orderDetailsBlob;
	}
	public Integer getOrderId() {
        return orderId;
    }
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getOrderPriceTax() {
        return orderPriceTax;
    }

    public void setOrderPriceTax(BigDecimal orderPriceTax) {
        this.orderPriceTax = orderPriceTax;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCountDown() {
        return countDown;
    }

    public void setCountDown(Date countDown) {
        this.countDown = countDown;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public List<OrderDetails> getOrderDetailsList() {
        return orderDetailsList;
    }

    public void setOrderDetailsList(List<OrderDetails> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }
}
