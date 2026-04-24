package com.warehouse.process.domain.port.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.ProcessLog;

import java.util.Optional;

public interface ProcessRepository {
    void create(final ProcessLog processLog);
    void update(final ProcessLog processLog);
    Optional<ProcessLog> findById(final ProcessId processId);
}
