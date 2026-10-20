package com.warehouse.asyncjob.application.port.primary.command;

import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.commonassets.identificator.OperatorId;

import java.time.LocalDate;

public record CreateReadModelRebuildJobCommand(ReadModelType readModelType,
                                               OperatorId operatorId,
                                               LocalDate dateFrom,
                                               LocalDate dateTo) {
}
