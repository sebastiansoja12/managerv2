package com.warehouse.create.infrastructure.adapter.secondary;

import com.warehouse.create.domain.model.TerminalRequest;
import com.warehouse.create.domain.model.TerminalResponse;
import com.warehouse.create.domain.port.secondary.RouteLogServicePort;
import lombok.AllArgsConstructor;


// TODO
@AllArgsConstructor
public class RouteLogServiceAdapter implements RouteLogServicePort {
    @Override
    public TerminalResponse logRoute(TerminalRequest request) {
        return null;
    }
}
