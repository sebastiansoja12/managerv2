package com.warehouse.process.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.vo.DeviceValidation;
import com.warehouse.process.domain.vo.ShipmentUpdated;

public interface ProcessService {
    ProcessId nextProcessId();
    void createProcess(final ProcessLog processLog);
    void updateResponse(final ProcessId processId, final String response);
    void logFinishedProcess(final ProcessId processId);
    void logFailedProcess(final ProcessId processId);
    ProcessLog findById(final ProcessId processId);
    ProcessLog findByIdForCurrentDepartment(final ProcessId processId);
    Page<ProcessLog> findAllForCurrentDepartment(final Pageable pageable);
    void assignShipmentUpdated(final ProcessId processId, final ShipmentUpdated shipmentUpdated);

    void assignDeviceValidation(final ProcessId processId, final DeviceValidation deviceValidation);
}
