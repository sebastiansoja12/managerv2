package com.warehouse.configuration;

public interface AddressingConfiguration {

    String getUrl();

    String getStage();

    String requestUrl(String value);
}
