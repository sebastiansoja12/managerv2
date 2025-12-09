package com.warehouse.supplier.infrastructure.adapter.secondary.api;

public record RegisterRequestDto(String email, String username, String password, String firstName, String lastName,
                                 String role, String departmentCode) {

}
