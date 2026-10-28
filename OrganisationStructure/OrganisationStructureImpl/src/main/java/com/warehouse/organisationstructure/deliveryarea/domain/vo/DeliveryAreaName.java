package com.warehouse.organisationstructure.deliveryarea.domain.vo;

import com.warehouse.organisationstructure.deliveryarea.domain.exception.InvalidDeliveryAreaNameException;

public record DeliveryAreaName(String value) {

    public static final int MAX_LENGTH = 120;

    public DeliveryAreaName {
        if (value == null || value.isBlank() || value.trim().length() > MAX_LENGTH) {
            throw new InvalidDeliveryAreaNameException(MAX_LENGTH);
        }
        value = value.trim();
    }
}
