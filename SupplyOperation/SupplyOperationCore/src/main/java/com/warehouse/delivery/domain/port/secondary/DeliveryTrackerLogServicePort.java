package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.delivery.domain.vo.DepartmentCodeRequest;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissed;
import com.warehouse.terminal.request.TerminalRequest;

public interface DeliveryTrackerLogServicePort {
    void logDeliveryTracker(final DeliveryMissed deliveryMissed);

    void logDepartmentCode(final DepartmentCodeRequest departmentCodeRequest);

    void logRequest(final TerminalRequest terminalRequest, final String requestAsJson);

    void logSupplierCode(final DeliveryMissed deliveryMissed);

    void logDeviceId(final TerminalRequest terminalRequest);

    void logVersion(final TerminalRequest terminalRequest);
}
