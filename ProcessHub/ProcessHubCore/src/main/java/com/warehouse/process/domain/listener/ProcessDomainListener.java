package com.warehouse.process.domain.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.process.domain.event.ProcessCreatedEvent;
import com.warehouse.process.domain.event.ProcessFinished;
import com.warehouse.process.domain.service.ProcessLogReadModelSyncService;
import com.warehouse.process.domain.vo.ProcessLogSnapshot;

@Component
public class ProcessDomainListener {

    private final ProcessLogReadModelSyncService processLogSyncService;

    public ProcessDomainListener(final ProcessLogReadModelSyncService processLogSyncService) {
        this.processLogSyncService = processLogSyncService;
    }

    @EventListener
    public void handle(final ProcessCreatedEvent event) {
        final ProcessLogSnapshot snapshot = event.getSnapshot();
        processLogSyncService.createProcessLog(snapshot);
    }

    @EventListener
    public void handle(final ProcessFinished event) {
        final ProcessLogSnapshot snapshot = event.getSnapshot();
        processLogSyncService.syncProcessLog(snapshot);
    }
}
