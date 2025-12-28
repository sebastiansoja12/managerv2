package com.warehouse.process.domain.port.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.ProcessLog;

public interface ProcessRepository {
    void create(final ProcessLog processLog);
    void update(final ProcessLog processLog);
    ProcessLog findById(final ProcessId processId);
}
