package com.warehouse.mcp.infrastructure.adapter.primary.response;

public record ShipmentPartyToolResponse(String initials,
                                        String email,
                                        String telephoneNumber,
                                        String city,
                                        String postalCode,
                                        String countryCode) {
}
