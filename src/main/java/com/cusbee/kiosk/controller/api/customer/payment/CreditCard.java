package com.cusbee.kiosk.controller.api.customer.payment;

/**
 * @author mxmind
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CreditCard {
    /**
     * @return the id
     */
    Long getId();

    /**
     * @param id the id to set
     */
    void setId(Long id);

    /**
     * @return the pan
     */
    String getPan();

    /**
     * @param pan the pan to set
     */
    void setPan(String pan);

    /**
     * @return the expirationMonth
     */
    Integer getExpirationMonth();

    /**
     * @param expirationMonth the expirationMonth to set
     */
    void setExpirationMonth(Integer expirationMonth);

    /**
     * @return the expirationYear
     */
    Integer getExpirationYear();

    /**
     * @param expirationYear the expirationYear to set
     */
    void setExpirationYear(Integer expirationYear);

    /**
     * @return the nameOnCard
     */
    String getNameOnCard();

    /**
     * @param nameOnCard the name on the card to set
     */
    void setNameOnCard(String nameOnCard);

    String getCvvCode();

    void setCvvCode(String cvvCode);

    void setEncryption(Encryption encryption);

    Encryption getEncryption();
}
