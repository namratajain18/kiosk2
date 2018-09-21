package com.cusbee.kiosk.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import com.cusbee.kiosk.entity.OrderDetails;
import com.cusbee.kiosk.entity.Orders;
import com.cusbee.kiosk.enums.OrderStatus;
import com.cusbee.kiosk.enums.PackageType;
import com.cusbee.kiosk.enums.PaymentStatus;

/**
 * Created by ahorbat on 12.02.17.
 */
public class OrderBean implements Serializable, Comparator<OrderBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Integer orderId;

	private String name;

	private Date date;

	private Double price = 0.0;

	private Double priceTax = 0.0;

	private String phone;

	private String address;

	private String source;

	private PaymentStatus status = PaymentStatus.NOT_PAYED;

	private PackageType type = PackageType.DINE_IN;

	private String email;

	private OrderStatus orderStatus;

	private Date countDown;

	private int table;

	private List<OrderDetails> orderDetails;

	private Blob orderDetailsBlob;
	
	private String paymentData;
	
	private Double discount = 0.0;

	
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public List<OrderDetails> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetails> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Double getPriceTax() {
		return priceTax;
	}

	public void setPriceTax(Double priceTax) {
		this.priceTax = priceTax;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCountDown() {
		return countDown;
	}

	public void setCountDown(Date countDown) {
		this.countDown = countDown;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public PackageType getType() {
		return type;
	}

	public void setType(PackageType type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getTable() {
		return table;
	}

	public void setTable(int table) {
		this.table = table;
	}

	public static OrderBean toOrderBeanWithDetails(Orders orders) {
		OrderBean bean = toOrderBean(orders);

		bean.setOrderDetails(orders.getOrderDetailsList());
		// orders.getOrderDetailsList().get(0).getModifiers().split(",");

		return bean;
	}

	public static OrderBean toOrderBean(Orders orders) {
		OrderBean bean = new OrderBean();

		bean.setId(orders.getId());
		bean.setOrderId(orders.getOrderId());
		bean.setPrice(orders.getOrderPrice().doubleValue());
		bean.setPriceTax(orders.getOrderPriceTax().doubleValue());
		bean.setName(orders.getName());
		bean.setTable(orders.getTable());
		bean.setAddress(orders.getAddress());
		bean.setSource(orders.getSource());
		bean.setOrderStatus(orders.getOrderStatus());
		bean.setOrderStatus(orders.getOrderStatus());
		if (orders.getOrderDate() == null) {
			bean.setDate(new Date());
		} else {
			bean.setDate(orders.getOrderDate());
			// bean.setDate(sdfDate.format(orders.getOrderDate()));
		}

		bean.setEmail(orders.getEmail());
		bean.setPhone(orders.getPhone());
		bean.setStatus(orders.getPaymentStatus());
		bean.setType(orders.getPackageType());
		bean.setPaymentData(orders.getPaymentData());
		bean.setDiscount(orders.getDiscount().doubleValue());
		if (orders.getCountDown() == null) {
			bean.setCountDown(new Date());
		} else {
			bean.setCountDown(orders.getCountDown());
			// bean.setCountDown(sdfDate.format(orders.getCountDown()));
		}
		// bean.setOrderDetails(orders.getOrderDetailsList());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(orders);
			byte[] orderdetail = baos.toByteArray();
			Blob blob = new SerialBlob(orderdetail);
			bean.setOrderDetailsBlob(blob);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SerialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * ByteArrayInputStream bais = new ByteArrayInputStream(employeeAsBytes); Blob
		 * orderdetailsBlob = (Blob) Hibernate.getLobCreator(session) .createBlob(bais,
		 * employeeAsBytes.length);
		 */
		// bean.setOrderDetailsBlob(employeeAsBytes);
		return bean;
	}

	public static List<OrderBean> toBeanListWithDetails(List<Orders> orderList) {
		List<OrderBean> orderBeanList = new ArrayList<>();
		for (Orders orders : orderList) {
			orderBeanList.add(OrderBean.toOrderBeanWithDetails(orders));
		}
		return orderBeanList;
	}

	public static List<OrderBean> toBeanList(List<Orders> orderList) {
		List<OrderBean> orderBeanList = new ArrayList<>();
		for (Orders orders : orderList) {
			orderBeanList.add(OrderBean.toOrderBean(orders));
		}
		return orderBeanList;
	}

	@Override
	public int compare(OrderBean o1, OrderBean o2) {
		return o1.getType().compareTo(o2.getType());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		OrderBean orderBean = (OrderBean) o;

		if (id != null ? !id.equals(orderBean.id) : orderBean.id != null) {
			return false;
		}
		if (name != null ? !name.equals(orderBean.name) : orderBean.name != null) {
			return false;
		}
		if (date != null ? !date.equals(orderBean.date) : orderBean.date != null) {
			return false;
		}
		if (price != null ? !price.equals(orderBean.price) : orderBean.price != null) {
			return false;
		}
		if (status != orderBean.status) {
			return false;
		}
		if (type != orderBean.type) {
			return false;
		}
		if (email != null ? !email.equals(orderBean.email) : orderBean.email != null) {
			return false;
		}
		return orderStatus == orderBean.orderStatus;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + (price != null ? price.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
		return result;
	}

}
