package com.warehouse.paypal.domain.model;

import com.paypal.api.payments.Transactions;
import lombok.Data;

import java.util.List;

@Data
public class PaymentExecution {
    private String payerId;
    private String carrierAccountId;
    private List<Transactions> transactions;
}
