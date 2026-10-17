package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.model.PlatformAuditEntry;

import java.util.List;

public interface PlatformAuditRepository {

    void save(final PlatformAuditEntry platformAuditEntry);

    List<PlatformAuditEntry> findLatest(final OperatorId operatorId, final int limit);
}
