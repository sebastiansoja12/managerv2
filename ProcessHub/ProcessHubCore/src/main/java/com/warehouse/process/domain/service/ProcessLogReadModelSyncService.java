package com.warehouse.process.domain.service;

import com.warehouse.process.domain.vo.ProcessLogSnapshot;

public interface ProcessLogReadModelSyncService {
    void createProcessLog(final ProcessLogSnapshot snapshot);
    void syncProcessLog(final ProcessLogSnapshot snapshot);
}
