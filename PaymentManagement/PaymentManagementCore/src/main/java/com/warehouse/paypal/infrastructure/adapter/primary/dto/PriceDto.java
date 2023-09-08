package com.warehouse.paypal.infrastructure.adapter.primary.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceDto {
    BigDecimal value;
}
