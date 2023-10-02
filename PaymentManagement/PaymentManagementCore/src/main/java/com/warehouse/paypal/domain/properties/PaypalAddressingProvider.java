package com.warehouse.paypal.domain.properties;

public interface PaypalAddressingProvider {
    String getUrl();

    String getMode();

    String clientId();

    String clientSecret();
}
