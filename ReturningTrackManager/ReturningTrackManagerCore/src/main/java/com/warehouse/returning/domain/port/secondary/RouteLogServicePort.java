package com.warehouse.returning.domain.port.secondary;

import com.warehouse.returning.domain.vo.ProcessReturn;

public interface RouteLogServicePort {
    void logReturn(ProcessReturn processReturn, String depotCode, String username);
}
