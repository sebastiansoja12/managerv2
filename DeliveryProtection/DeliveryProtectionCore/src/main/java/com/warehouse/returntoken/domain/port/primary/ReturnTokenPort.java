package com.warehouse.returntoken.domain.port.primary;

import com.warehouse.returntoken.domain.model.ReturnTokenResponse;
import com.warehouse.returntoken.domain.vo.ReturnTokenRequest;

public interface ReturnTokenPort {
    ReturnTokenResponse signWith(final ReturnTokenRequest request);
}
