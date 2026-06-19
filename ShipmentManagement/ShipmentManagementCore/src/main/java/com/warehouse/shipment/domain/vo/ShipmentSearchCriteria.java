package com.warehouse.shipment.domain.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.searchobject.SearchCriteria;

public class ShipmentSearchCriteria extends SearchCriteria {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 50;
    private static final int MAX_SIZE = 100;

    private final Long shipmentId;
    private final String trackingNumber;
    private final List<ShipmentStatus> shipmentStatuses;
    private final List<ShipmentSize> shipmentSizes;
    private final List<ShipmentPriority> shipmentPriorities;
    private final String senderName;
    private final String recipientName;
    private final String destination;
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;
    private final Currency currency;
    private final Boolean locked;
    private final LocalDateTime createdFrom;
    private final LocalDateTime createdTo;
    private final Integer page;
    private final Integer size;

    public ShipmentSearchCriteria(
            final Long shipmentId,
            final String trackingNumber,
            final List<ShipmentStatus> shipmentStatuses,
            final List<ShipmentSize> shipmentSizes,
            final List<ShipmentPriority> shipmentPriorities,
            final String senderName,
            final String recipientName,
            final String destination,
            final BigDecimal minPrice,
            final BigDecimal maxPrice,
            final Currency currency,
            final Boolean locked,
            final LocalDateTime createdFrom,
            final LocalDateTime createdTo,
            final Integer page,
            final Integer size
    ) {
        this.shipmentId = shipmentId;
        this.trackingNumber = trackingNumber;
        this.shipmentStatuses = shipmentStatuses;
        this.shipmentSizes = shipmentSizes;
        this.shipmentPriorities = shipmentPriorities;
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.destination = destination;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.currency = currency;
        this.locked = locked;
        this.createdFrom = createdFrom;
        this.createdTo = createdTo;
        this.page = page;
        this.size = size;
    }

    public int pageNumber() {
        return page == null || page < 0 ? DEFAULT_PAGE : page;
    }

    public int pageSize() {
        if (size == null || size <= 0) {
            return DEFAULT_SIZE;
        }
        return Math.min(size, MAX_SIZE);
    }

    public Long shipmentId() {
        return shipmentId;
    }

    public String trackingNumber() {
        return trackingNumber;
    }

    public List<ShipmentStatus> shipmentStatuses() {
        return shipmentStatuses;
    }

    public List<ShipmentSize> shipmentSizes() {
        return shipmentSizes;
    }

    public List<ShipmentPriority> shipmentPriorities() {
        return shipmentPriorities;
    }

    public String senderName() {
        return senderName;
    }

    public String recipientName() {
        return recipientName;
    }

    public String destination() {
        return destination;
    }

    public BigDecimal minPrice() {
        return minPrice;
    }

    public BigDecimal maxPrice() {
        return maxPrice;
    }

    public Currency currency() {
        return currency;
    }

    public Boolean locked() {
        return locked;
    }

    public LocalDateTime createdFrom() {
        return createdFrom;
    }

    public LocalDateTime createdTo() {
        return createdTo;
    }

    public Integer page() {
        return page;
    }

    public Integer size() {
        return size;
    }
}
