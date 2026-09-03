package com.warehouse.platformadministration.application.port.primary.command;

public record RecordAuditedRequestCommand(String httpMethod, String path, int statusCode, boolean failed) {
}
