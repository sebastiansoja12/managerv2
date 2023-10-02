package com.warehouse.paypal.infrastructure.adapter.primary.dto;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class PriceDto {
    BigDecimal value;
}
