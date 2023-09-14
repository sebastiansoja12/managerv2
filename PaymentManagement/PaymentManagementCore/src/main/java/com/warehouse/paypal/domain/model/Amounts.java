package com.warehouse.paypal.domain.model;

import lombok.Data;

@Data
public class Amounts {
    private String currency_code;
    private String value;
}
