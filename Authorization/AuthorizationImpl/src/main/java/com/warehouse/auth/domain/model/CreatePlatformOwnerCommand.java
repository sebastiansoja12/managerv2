package com.warehouse.auth.domain.model;

public record CreatePlatformOwnerCommand(String username, String password, String firstName, String lastName,
                                         String email) {
}
