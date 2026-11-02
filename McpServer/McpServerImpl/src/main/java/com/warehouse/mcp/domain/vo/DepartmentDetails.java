package com.warehouse.mcp.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record DepartmentDetails(DepartmentCode departmentCode,
                                String city,
                                String street,
                                String zipCode,
                                String countryCode,
                                Double latitude,
                                Double longitude) {
}
