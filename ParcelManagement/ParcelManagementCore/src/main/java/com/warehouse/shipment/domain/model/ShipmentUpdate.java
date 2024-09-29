package com.warehouse.shipment.domain.model;

import com.warehouse.shipment.domain.vo.ShipmentUpdateRequest;

public class ShipmentUpdate {
    
    private Long id;

    private String senderFirstName;

    private String senderLastName;

    private String senderTelephone;

    private String senderEmail;

    private String senderCity;

    private String senderStreet;

    private String senderPostalCode;

    private String recipientFirstName;

    private String recipientLastName;

    private String recipientTelephone;

    private String recipientEmail;

    private String recipientCity;

    private String recipientStreet;

    private String recipientPostalCode;

    private String destination;

	public ShipmentUpdate(final Long id, final String senderFirstName, final String senderLastName,
			final String senderTelephone, final String senderEmail, final String senderCity, final String senderStreet,
			final String senderPostalCode, final String recipientFirstName, final String recipientLastName,
			final String recipientTelephone, final String recipientEmail, final String recipientCity,
			final String recipientStreet, final String recipientPostalCode, final String destination) {
		this.id = id;
		this.senderFirstName = senderFirstName;
		this.senderLastName = senderLastName;
		this.senderTelephone = senderTelephone;
		this.senderEmail = senderEmail;
		this.senderCity = senderCity;
		this.senderStreet = senderStreet;
		this.senderPostalCode = senderPostalCode;
		this.recipientFirstName = recipientFirstName;
		this.recipientLastName = recipientLastName;
		this.recipientTelephone = recipientTelephone;
		this.recipientEmail = recipientEmail;
		this.recipientCity = recipientCity;
		this.recipientStreet = recipientStreet;
		this.recipientPostalCode = recipientPostalCode;
		this.destination = destination;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getSenderTelephone() {
        return senderTelephone;
    }

    public void setSenderTelephone(String senderTelephone) {
        this.senderTelephone = senderTelephone;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getSenderStreet() {
        return senderStreet;
    }

    public void setSenderStreet(String senderStreet) {
        this.senderStreet = senderStreet;
    }

    public String getSenderPostalCode() {
        return senderPostalCode;
    }

    public void setSenderPostalCode(String senderPostalCode) {
        this.senderPostalCode = senderPostalCode;
    }

    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public void setRecipientFirstName(String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
    }

    public String getRecipientLastName() {
        return recipientLastName;
    }

    public void setRecipientLastName(String recipientLastName) {
        this.recipientLastName = recipientLastName;
    }

    public String getRecipientTelephone() {
        return recipientTelephone;
    }

    public void setRecipientTelephone(String recipientTelephone) {
        this.recipientTelephone = recipientTelephone;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientCity() {
        return recipientCity;
    }

    public void setRecipientCity(String recipientCity) {
        this.recipientCity = recipientCity;
    }

    public String getRecipientStreet() {
        return recipientStreet;
    }

    public void setRecipientStreet(String recipientStreet) {
        this.recipientStreet = recipientStreet;
    }

    public String getRecipientPostalCode() {
        return recipientPostalCode;
    }

    public void setRecipientPostalCode(String recipientPostalCode) {
        this.recipientPostalCode = recipientPostalCode;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void updateDestination(final String destination) {
        this.destination = destination;
    }

    public static ShipmentUpdate from(final ShipmentUpdateRequest request) {
        return request.getParcelUpdate();
    }
}
