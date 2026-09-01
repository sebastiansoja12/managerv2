package com.warehouse.platformadministration.application.port.primary.command;

public record CreatePlatformOwnerAccountCommand(String username,
                                                String password,
                                                String firstName,
                                                String lastName,
                                                String email) {
}
