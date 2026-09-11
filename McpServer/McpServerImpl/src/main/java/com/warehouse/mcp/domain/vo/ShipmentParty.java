package com.warehouse.mcp.domain.vo;

public record ShipmentParty(String firstName,
                            String lastName,
                            String email,
                            String telephoneNumber,
                            String city,
                            String postalCode,
                            String countryCode) {
}
