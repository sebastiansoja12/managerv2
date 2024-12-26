package com.warehouse.returntoken.domain.port.primary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warehouse.returntoken.domain.model.ReturnTokenResponse;
import com.warehouse.returntoken.domain.service.ReturnTokenService;
import com.warehouse.returntoken.domain.vo.ReturnTokenRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnTokenPortImpl implements ReturnTokenPort {

    private final ReturnTokenService returnTokenService;

    private final Logger log = LoggerFactory.getLogger("ReturnToken");

    // TODO
    @Override
    public ReturnTokenResponse signWith(final ReturnTokenRequest request) {
        return null;
    }
}
