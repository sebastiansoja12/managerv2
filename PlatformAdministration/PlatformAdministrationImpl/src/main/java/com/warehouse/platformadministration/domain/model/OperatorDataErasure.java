package com.warehouse.platformadministration.domain.model;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.exception.OperatorDataErasureNotAllowedException;
import com.warehouse.platformadministration.domain.vo.OperatorProfile;

import java.time.Instant;

public record OperatorDataErasure(Long id,
                                  OperatorId operatorId,
                                  String requestedBy,
                                  Instant executedAt,
                                  int usersAnonymized,
                                  int couriersAnonymized,
                                  int devicesRetired) {

    public static final String OPERATOR_ACTIVE = "OPERATOR_ACTIVE";

    public static final String CONFIRMATION_MISMATCH = "CONFIRMATION_MISMATCH";

    public static void verify(final OperatorProfile operator, final String confirmation) {
        if (!operator.blocked()) {
            throw new OperatorDataErasureNotAllowedException(OPERATOR_ACTIVE,
                    "Operator must be blocked before its data can be erased");
        }
        if (confirmation == null || !confirmation.trim().equals(operator.companyName())) {
            throw new OperatorDataErasureNotAllowedException(CONFIRMATION_MISMATCH,
                    "Confirmation must match the operator company name");
        }
    }

    public static OperatorDataErasure completed(final OperatorId operatorId,
                                                final String requestedBy,
                                                final Instant executedAt,
                                                final int usersAnonymized,
                                                final int couriersAnonymized,
                                                final int devicesRetired) {
        return new OperatorDataErasure(null, operatorId, requestedBy, executedAt, usersAnonymized,
                couriersAnonymized, devicesRetired);
    }
}
