package com.warehouse.returning.domain.vo;

public record Address(
        String street,
        String city,
        String postalCode,
        String state,
        String country,
        String building,
        String apartment,
        String phoneNumber
) {}

