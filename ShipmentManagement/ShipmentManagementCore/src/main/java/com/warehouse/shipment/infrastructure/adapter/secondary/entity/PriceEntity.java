package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.ShipmentSize;

import com.warehouse.commonassets.model.Money;
import jakarta.persistence.*;

@Entity
@Table(name = "price")
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_seq")
    @SequenceGenerator(name = "price_seq", sequenceName = "price_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_size", nullable = false)
    private ShipmentSize shipmentSize;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency_code"))
    })
    private Money price;

    public PriceEntity() {
    }

    public PriceEntity(final Long id, final Money price, final ShipmentSize shipmentSize) {
        this.id = id;
        this.price = price;
        this.shipmentSize = shipmentSize;
    }

    public Long getId() {
        return id;
    }

    public Money getPrice() {
        return price;
    }

    public ShipmentSize getShipmentSize() {
        return shipmentSize;
    }
}
