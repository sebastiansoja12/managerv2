package com.warehouse.paypal.domain.model;

import lombok.Data;

@Data
public class PurchaseUnit {
    public String reference_id;
    public Amounts amount;
}
