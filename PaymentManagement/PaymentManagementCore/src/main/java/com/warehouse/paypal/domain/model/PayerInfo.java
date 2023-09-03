package com.warehouse.paypal.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayerInfo {
    private String email;
    private String externalRememberMeId;
    private String accountNumber;
    private String salutation;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String payerId;
    private String phone;
    private String phoneType;
    private String birthDate;
    private String taxId;
    private String taxIdType;
    private String countryCode;
}
