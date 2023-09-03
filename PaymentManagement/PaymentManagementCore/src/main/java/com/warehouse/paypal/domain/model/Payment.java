package com.warehouse.paypal.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Payment {
    private Long parcelId;
    private BigDecimal price;
    private String email;
    private String firstName;
    private String lastName;
}
