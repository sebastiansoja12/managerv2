package com.warehouse.paypal.domain.model;

import lombok.Data;

@Data
public class Payee {
    private String email;
    private String merchantId;
    private String firstName;
    private String lastName;
    private String accountNumber;
}
