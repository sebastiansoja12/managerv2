package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.enumeration.Status;


public class ReturnStatusMapper {

    public static ReturnStatus toModelStatus(final Status status) {
        return switch (status) {
            case CREATED -> ReturnStatus.CREATED;
            case PROCESSING -> ReturnStatus.PROCESSING;
            case COMPLETED -> ReturnStatus.COMPLETED;
            case CANCELLED -> ReturnStatus.CANCELLED;
            case null -> throw new IllegalArgumentException("Status cannot be null");
        };
    }

    public static Status toEntityStatus(final ReturnStatus returnStatus) {
        return switch (returnStatus) {
            case CREATED -> Status.CREATED;
            case PROCESSING -> Status.PROCESSING;
            case COMPLETED -> Status.COMPLETED;
            case CANCELLED -> Status.CANCELLED;
            case null -> throw new IllegalArgumentException("ReturnStatus cannot be null");
        };
    }
}
