package com.warehouse.mcp.infrastructure.adapter.primary.response;

public record DepartmentDetailsToolResponse(String departmentCode,
                                            String city,
                                            String street,
                                            String zipCode,
                                            String countryCode,
                                            Double latitude,
                                            Double longitude) {
}
