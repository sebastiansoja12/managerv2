package com.warehouse.paypal.domain.model;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Getter
@ConfigurationProperties(prefix = "redirect")
public class RedirectUrls {

    private String returnUrl;
    private String cancelUrl;

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}
