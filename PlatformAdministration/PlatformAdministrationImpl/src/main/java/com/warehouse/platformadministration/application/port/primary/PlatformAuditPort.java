package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.primary.command.RecordAuditedRequestCommand;
import com.warehouse.platformadministration.domain.model.PlatformAuditEntry;

import java.util.List;

public interface PlatformAuditPort {

    void record(final RecordAuditedRequestCommand command);

    List<PlatformAuditEntry> findLatest(final OperatorId operatorId, final int limit);
}
