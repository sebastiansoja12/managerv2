package com.warehouse.mcp.domain.vo;

public record MaskedShipmentParty(String initials,
                                  String email,
                                  String telephoneNumber,
                                  String city,
                                  String postalCode,
                                  String countryCode) {
}
