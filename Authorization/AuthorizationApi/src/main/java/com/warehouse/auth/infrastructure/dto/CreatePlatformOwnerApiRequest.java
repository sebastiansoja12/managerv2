package com.warehouse.auth.infrastructure.dto;

public record CreatePlatformOwnerApiRequest(String username, String password, String firstName, String lastName,
                                            String email) {
}
