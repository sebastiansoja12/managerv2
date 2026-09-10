package com.warehouse.asyncjob.application.port.secondary;

import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.commonassets.identificator.OperatorId;

import java.time.LocalDate;

public interface ReadModelRebuildServicePort {

    ReadModelType type();

    void rebuild(final OperatorId operatorId, final LocalDate dateFrom, final LocalDate dateTo);
}
