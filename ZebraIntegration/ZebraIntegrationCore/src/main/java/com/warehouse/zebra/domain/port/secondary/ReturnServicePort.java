package com.warehouse.zebra.domain.port.secondary;


import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.response.Response;

public interface ReturnServicePort {
    Response processReturn(final Request zebraRequest);
}
