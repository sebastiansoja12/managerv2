package com.warehouse.create.domain.port.secondary;

import com.warehouse.create.domain.model.DeliveryCreate;
import com.warehouse.create.domain.model.TerminalRequest;

public interface DeliveryCreateRepository {
    DeliveryCreate save(TerminalRequest request);
}
