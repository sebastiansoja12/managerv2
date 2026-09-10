package com.warehouse.asyncjob.domain.service;

import java.time.LocalDate;

import com.warehouse.asyncjob.domain.model.ReadModelType;

public interface ReadModelRebuilder {

    ReadModelType type();

    void rebuild(
            Long operatorId,
            LocalDate dateFrom,
            LocalDate dateTo
    );
}
