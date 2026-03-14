package com.warehouse.process.domain.service;

import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.port.secondary.ProcessLogReadModelRepository;
import com.warehouse.process.domain.vo.ProcessLogSnapshot;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ProcessLogReadModelSyncServiceImpl implements ProcessLogReadModelSyncService {

    private final ProcessLogReadModelRepository processLogRepository;

    public ProcessLogReadModelSyncServiceImpl(final ProcessLogReadModelRepository processLogRepository) {
        this.processLogRepository = processLogRepository;
    }

    @Override
    public void createProcessLog(final ProcessLogSnapshot snapshot) {
        final ProcessLog processLog = ProcessLog.builder()
                .processId(snapshot.processId())
                .request(snapshot.request())
                .response(snapshot.response())
                .createdAt(snapshot.createdAt())
                .modifiedAt(snapshot.modifiedAt())
                .communicationLogDetails(snapshot.communicationLogDetails())
                .status(snapshot.status())
                .faultDescription(snapshot.faultDescription())
                .deviceInformation(snapshot.deviceInformation())
                .build();

        this.processLogRepository.createProcessLog(processLog);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void syncProcessLog(final ProcessLogSnapshot snapshot) {
        final ProcessLog processLog = this.processLogRepository.findById(snapshot.processId())
                .orElseGet(() -> ProcessLog.builder()
                        .processId(snapshot.processId())
                        .build());
        processLog.applySnapshot(snapshot);
        this.processLogRepository.syncProcessLog(processLog);
    }
}
