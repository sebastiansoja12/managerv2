package com.warehouse.returning.domain.port.primary;

import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.vo.ReturnResponse;

public interface ReturnPort {
    ReturnResponse process(ReturnRequest request);
}
