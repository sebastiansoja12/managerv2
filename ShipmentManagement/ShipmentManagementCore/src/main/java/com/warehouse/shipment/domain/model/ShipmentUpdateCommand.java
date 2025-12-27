package com.warehouse.shipment.domain.model;

import java.util.Objects;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentConfiguration;

import lombok.Builder;

@Builder
public class ShipmentUpdateCommand {
    
    private ShipmentId shipmentId;
    
    private Sender sender;

    private Recipient recipient;

    private String destination;

    private ShipmentSize shipmentSize;

    private Money price;

    private DangerousGood dangerousGood;

    private CountryCode issuerCountryCode;

    private CountryCode receiverCountryCode;

    private ShipmentPriority shipmentPriority;

    private ShipmentStatus shipmentStatus;

    private ShipmentConfiguration shipmentConfiguration;

    public ShipmentUpdateCommand() {
    }

    public ShipmentUpdateCommand(
            final ShipmentId shipmentId,
            final Sender sender,
            final Recipient recipient,
            final String destination,
            final ShipmentSize shipmentSize,
            final Money price,
            final DangerousGood dangerousGood,
            final CountryCode issuerCountryCode,
            final CountryCode receiverCountryCode,
            final ShipmentPriority shipmentPriority,
            final ShipmentStatus shipmentStatus,
            final ShipmentConfiguration shipmentConfiguration
    ) {
        this.shipmentId = shipmentId;
        this.sender = sender;
        this.recipient = recipient;
        this.destination = destination;
        this.shipmentSize = shipmentSize;
        this.price = price;
        this.dangerousGood = dangerousGood;
        this.issuerCountryCode = issuerCountryCode;
        this.receiverCountryCode = receiverCountryCode;
        this.shipmentPriority = shipmentPriority;
        this.shipmentStatus = shipmentStatus;
        this.shipmentConfiguration = shipmentConfiguration;
    }

    public DangerousGood getDangerousGood() {
        return dangerousGood;
    }

    public void setDangerousGood(final DangerousGood dangerousGood) {
        this.dangerousGood = dangerousGood;
    }

    public CountryCode getIssuerCountryCode() {
        return issuerCountryCode;
    }

    public void setIssuerCountryCode(final CountryCode issuerCountryCode) {
        this.issuerCountryCode = issuerCountryCode;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(final Money price) {
        this.price = price;
    }

    public CountryCode getReceiverCountryCode() {
        return receiverCountryCode;
    }

    public void setReceiverCountryCode(final CountryCode receiverCountryCode) {
        this.receiverCountryCode = receiverCountryCode;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(final Recipient recipient) {
        this.recipient = recipient;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(final Sender sender) {
        this.sender = sender;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public ShipmentPriority getShipmentPriority() {
        return shipmentPriority;
    }

    public void setShipmentPriority(final ShipmentPriority shipmentPriority) {
        this.shipmentPriority = shipmentPriority;
    }

    public ShipmentSize getShipmentSize() {
        return shipmentSize;
    }

    public void setShipmentSize(final ShipmentSize shipmentSize) {
        this.shipmentSize = shipmentSize;
    }

    public ShipmentConfiguration getShipmentConfiguration() {
        return Objects.requireNonNullElse(
                shipmentConfiguration,
                ShipmentConfiguration.defaults()
        );
    }

    public void setShipmentConfiguration(final ShipmentConfiguration shipmentConfiguration) {
        this.shipmentConfiguration = shipmentConfiguration;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(final ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(final String destination) {
        this.destination = destination;
    }
}
