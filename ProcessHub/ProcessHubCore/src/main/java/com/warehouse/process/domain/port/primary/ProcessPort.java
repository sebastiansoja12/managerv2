package com.warehouse.process.domain.port.primary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.InitializeProcessCommand;
import com.warehouse.process.domain.model.ProcessDeviceValidatedCommand;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.domain.vo.ChangeResponseProcessCommand;
import com.warehouse.process.domain.vo.ProcessCommunication;
import com.warehouse.process.domain.vo.ShipmentRejected;
import com.warehouse.process.domain.vo.ShipmentUpdated;

public interface ProcessPort {
    ProcessId initialize(final InitializeProcessCommand command);
    void changeResponse(final ChangeResponseProcessCommand command);
    void finishProcess(final ProcessId processId, final ProcessStatus processStatus);
    void finishProcess(final ProcessId processId, final ProcessStatus processStatus, final String faultDescription);

    void assignShipmentUpdated(final ProcessId processId, final ShipmentUpdated shipmentUpdated);

    void assignShipmentRejected(final ProcessId processId, final ShipmentRejected shipmentRejected);

    void assignProcessDeviceValidation(final ProcessDeviceValidatedCommand command);

    void assignCommunication(final ProcessId processId, final ProcessCommunication communication);

    ProcessLog findByIdForCurrentDepartment(final ProcessId processId);

    Page<ProcessLog> findAllForCurrentDepartment(final Pageable pageable);
}
