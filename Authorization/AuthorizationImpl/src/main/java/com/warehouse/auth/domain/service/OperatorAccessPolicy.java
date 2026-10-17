package com.warehouse.auth.domain.service;

import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Instant;

@FunctionalInterface
public interface OperatorAccessPolicy {

    boolean allowsAccess(final OperatorId operatorId, final Instant issuedAt, final boolean impersonation);
}
