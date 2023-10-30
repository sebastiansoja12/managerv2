package com.warehouse.zebra.domain.port.primary;

import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.domain.vo.Response;

public interface ZebraPort {
    Response processRequest(Request zebraRequest);
}
