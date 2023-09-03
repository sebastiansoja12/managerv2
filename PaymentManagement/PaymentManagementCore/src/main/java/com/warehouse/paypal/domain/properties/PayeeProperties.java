package com.warehouse.paypal.domain.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
@ConfigurationProperties(prefix = "payee")
public class PayeeProperties {
    private String email;
    private String merchantId;
    private String firstName;
    private String lastName;
    private String accountNumber;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
