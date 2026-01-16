package com.warehouse.process.domain.service;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.vo.ShipmentUpdated;

public interface ProcessService {
    ProcessId nextProcessId();
    void createProcess(final ProcessLog processLog);
    void updateResponse(final ProcessId processId, final String response);
    void logFinishedProcess(final ProcessId processId);
    void logFailedProcess(final ProcessId processId);
    ProcessLog findById(final ProcessId processId);
    void assignShipmentUpdated(final ProcessId processId, final ShipmentUpdated shipmentUpdated);
}
