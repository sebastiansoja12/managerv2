package com.warehouse.zebra.domain.port.primary;

import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.domain.vo.Response;

public interface ZebraInitializePort {
    Response processRequest(Request request);
}
