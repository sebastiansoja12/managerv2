package com.warehouse.platformadministration.application.port.primary.command;

import java.time.LocalDate;

public record StartReadModelRebuildCommand(String readModelType, LocalDate dateFrom, LocalDate dateTo) {
}
