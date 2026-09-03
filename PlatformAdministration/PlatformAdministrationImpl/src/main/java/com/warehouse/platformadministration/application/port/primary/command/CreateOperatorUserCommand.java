package com.warehouse.platformadministration.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentCode;

public record CreateOperatorUserCommand(String firstName,
                                        String lastName,
                                        String username,
                                        String password,
                                        String email,
                                        String role,
                                        DepartmentCode departmentCode,
                                        String language) {
}
