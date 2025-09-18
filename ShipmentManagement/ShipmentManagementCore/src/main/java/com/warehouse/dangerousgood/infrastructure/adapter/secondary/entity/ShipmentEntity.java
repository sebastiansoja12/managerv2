package com.warehouse.dangerousgood.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.ShipmentId;
import jakarta.persistence.*;

@Table(name = "shipment")
@Entity(name = "dangerousGood.shipmentEntity")
public class ShipmentEntity {

    @Column(name = "shipment_id")
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id"))
    private ShipmentId shipmentId;

    @Column(name = "origin_country", nullable = false)
    @Enumerated(EnumType.STRING)
    private Country issuerCountry;

    @Column(name = "destination_country", nullable = false)
    @Enumerated(EnumType.STRING)
    private Country receiverCountry;

    public ShipmentEntity() {
    }

    public ShipmentEntity(final Country issuerCountry, final Country receiverCountry, final ShipmentId shipmentId) {
        this.issuerCountry = issuerCountry;
        this.receiverCountry = receiverCountry;
        this.shipmentId = shipmentId;
    }

    public Country getIssuerCountry() {
        return issuerCountry;
    }

    public void setIssuerCountry(final Country issuerCountry) {
        this.issuerCountry = issuerCountry;
    }

    public Country getReceiverCountry() {
        return receiverCountry;
    }

    public void setReceiverCountry(final Country receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }
}
