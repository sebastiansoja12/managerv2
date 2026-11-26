package com.warehouse.logisticsdelivery.application.port.primary.result;

public record AssignmentOrchestration(int configuredDepartments, int triggeredDepartments, int disabledDepartments,
                                      int departmentsWithActiveRun, int departmentsWithEmptyWindow) {
}
