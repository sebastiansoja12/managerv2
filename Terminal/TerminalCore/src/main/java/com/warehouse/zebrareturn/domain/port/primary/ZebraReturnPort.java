package com.warehouse.zebrareturn.domain.port.primary;

import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.response.Response;

public interface ZebraReturnPort {
    Response processRequest(final Request zebraRequest);
}
