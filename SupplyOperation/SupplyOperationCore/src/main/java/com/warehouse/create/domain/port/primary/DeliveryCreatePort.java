package com.warehouse.create.domain.port.primary;

import com.warehouse.create.domain.model.TerminalRequest;
import com.warehouse.create.domain.model.TerminalResponse;

public interface DeliveryCreatePort {
    TerminalResponse createDelivery(TerminalRequest request);
}
