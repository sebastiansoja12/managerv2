package com.warehouse.shipment.infrastructure.adapter.primary.api;

public record CountryRequest(String issuerCountryCode, String receiverCountryCode) {
}
