package com.warehouse.paypal.domain.model;

import lombok.Data;

@Data
public class TransactionBase extends CartBase {
    private String purchaseUnitReferenceId;
}
