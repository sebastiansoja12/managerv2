package com.warehouse.paypal.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaypalRequest {
    private String intent;
    private List<Transaction> transaction;
    private RedirectUrls redirectUrls;
    private Payer payer;
}
