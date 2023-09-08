package com.warehouse.paypal.domain.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "redirect")
public class RedirectUrls {
    private String returnUrl = "/v2/api/payments/success";
    private String cancelUrl = "/v2/api/payments/error";
}
