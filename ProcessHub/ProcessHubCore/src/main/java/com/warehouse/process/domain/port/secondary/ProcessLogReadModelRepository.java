package com.warehouse.process.domain.port.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.ProcessLog;

public interface ProcessLogReadModelRepository {
    void createProcessLog(final ProcessLog processLog);
    Optional<ProcessLog> findById(final ProcessId processId);
    void syncProcessLog(final ProcessLog processLog);
}
