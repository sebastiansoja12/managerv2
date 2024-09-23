package com.warehouse.zebra.domain.port.primary;

import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.response.Response;

public interface ZebraInitializePort {
    Response processRequest(final Request request);
}
