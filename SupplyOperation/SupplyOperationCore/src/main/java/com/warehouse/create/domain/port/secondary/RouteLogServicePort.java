package com.warehouse.create.domain.port.secondary;

import com.warehouse.create.domain.model.TerminalRequest;
import com.warehouse.create.domain.model.TerminalResponse;

public interface RouteLogServicePort {
    TerminalResponse logRoute(TerminalRequest request);
}
