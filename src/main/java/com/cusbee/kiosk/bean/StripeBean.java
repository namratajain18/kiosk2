package com.cusbee.kiosk.bean;

/**
 * Created by ahorbat on 01.05.17.
 */
public class StripeBean {
    private Integer amount;
    private String number;
    private Long cardId;
    private String cardHolderName;
    private String cardHolderCellPhone;
    private Integer expMonth;
    private Integer expYear;
    private Integer cvv;
    private String key;
    private Long orderId;
    private Long locId;

    
    public Long getLocId() {
		return locId;
	}

	public void setLocId(Long locId) {
		this.locId = locId;
	}

	public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardHolderCellPhone() {
        return cardHolderCellPhone;
    }

    public void setCardHolderCellPhone(String cardHolderCellPhone) {
        this.cardHolderCellPhone = cardHolderCellPhone;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
