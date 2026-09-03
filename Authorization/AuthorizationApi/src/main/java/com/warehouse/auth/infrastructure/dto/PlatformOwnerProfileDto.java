package com.warehouse.auth.infrastructure.dto;

public record PlatformOwnerProfileDto(Long platformOwnerId, String username, String firstName, String lastName,
                                      String email) {
}
