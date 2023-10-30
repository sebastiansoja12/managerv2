package com.warehouse.zebra.domain.port.secondary;

import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.domain.vo.Response;

public interface ReturnServicePort {
    Response processReturn(Request zebraRequest);
}
