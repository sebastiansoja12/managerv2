package com.warehouse.mcp.infrastructure.adapter.primary.response;

public record CourierToolResponse(String courierCode,
                                  String firstName,
                                  String lastName,
                                  String telephoneNumber,
                                  String departmentCode,
                                  String status) {
}
