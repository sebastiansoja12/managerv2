package com.warehouse.paypal.domain.model;


import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaypalResponse {
    public String id;
    public String intent;
    public Payer payer;
    public String state;
    public Date create_time;
    private List<Links> links;
    private List<Transaction> transactions;
    private String createTime;
    private String paymentMethod;
    private String failureReason;
}
