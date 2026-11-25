package com.warehouse.auth.domain.model;

public record CreateBusinessCustomerUserCommand(
        String username,
        String name,
        String email,
        String language) {
}
