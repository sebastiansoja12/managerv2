package com.warehouse.paypal.domain.model;


import com.paypal.api.payments.Transaction;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaypalResponse {
    private List<Links> links;
    private List<Transaction> transactions;
    private String createTime;
    private String paymentMethod;
    private String failureReason;
}
