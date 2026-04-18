package com.warehouse.auth.infrastructure.dto;

public record RegisteringUserDto(String firstName, String lastName, String username, String password, String language,
                                 String email, DepartmentCodeDto departmentCode,
                                 OperatorIdDto operatorId) {
}
