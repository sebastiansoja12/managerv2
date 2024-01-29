package com.warehouse.create.domain.service;

import com.warehouse.create.domain.model.TerminalRequest;
import com.warehouse.create.domain.model.TerminalResponse;

public interface DeliveryCreateService {
    TerminalResponse createDelivery(TerminalRequest request);
}
