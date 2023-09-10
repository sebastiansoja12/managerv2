package com.warehouse.paypal.domain.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "redirect")
public class RedirectUrls {
    private String returnUrl;
    private String cancelUrl;
}
