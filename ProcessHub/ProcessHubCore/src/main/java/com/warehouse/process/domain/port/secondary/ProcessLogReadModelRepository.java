package com.warehouse.process.domain.port.secondary;

import com.warehouse.process.domain.model.ProcessLog;

public interface ProcessLogReadModelRepository {
    void createProcessLog(final ProcessLog processLog);
}
