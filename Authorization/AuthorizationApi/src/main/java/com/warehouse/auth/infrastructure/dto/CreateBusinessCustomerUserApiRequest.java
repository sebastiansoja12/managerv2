package com.warehouse.auth.infrastructure.dto;

public record CreateBusinessCustomerUserApiRequest(String username, String name, String email, String language) {
}
