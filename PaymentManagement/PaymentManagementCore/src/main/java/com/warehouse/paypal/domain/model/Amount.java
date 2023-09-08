package com.warehouse.paypal.domain.model;

import com.warehouse.paypal.domain.enumeration.Currency;
import lombok.Data;

@Data
public class Amount {
    private Currency currency = Currency.PLN;
    private String total;
    private Details details;

    public Amount(String total, Details details) {
        this.total = total;
        this.details = details;
    }
}
