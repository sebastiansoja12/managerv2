package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.delivery.domain.vo.DepartmentCodeRequest;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.terminal.enumeration.ProcessType;
import com.warehouse.terminal.request.TerminalRequest;

import java.util.Set;

public interface DeliveryTrackerLogServicePort {
    void logDeliveryTracker(final DeliveryMissed deliveryMissed);

    void logDepartmentCode(final DepartmentCodeRequest departmentCodeRequest);

    void logRequest(final TerminalRequest terminalRequest, final String requestAsJson);

    void logSupplierCode(final DeliveryMissed deliveryMissed);

    void logDeviceId(final TerminalRequest terminalRequest);

    void logVersion(final TerminalRequest terminalRequest);

    void logDeviceInformation(final Set<ShipmentId> shipmentIds, final DeviceInformation deviceInformation, final ProcessType processType);
}
