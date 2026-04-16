package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.vo.DecodedApiTenant;

public interface JwtDecodeService {
    DecodedApiTenant decodeJwt(final String token);
}
