package com.warehouse.palletmanagement.application.port.secondary;

import com.warehouse.palletmanagement.domain.vo.PalletAuditLog;

public interface AuditLogServicePort {

    void record(final PalletAuditLog palletAuditLog);
}
