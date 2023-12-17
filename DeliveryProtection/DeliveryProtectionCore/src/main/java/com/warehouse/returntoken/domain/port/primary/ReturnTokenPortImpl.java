package com.warehouse.returntoken.domain.port.primary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.warehouse.returntoken.domain.model.Parcel;
import com.warehouse.returntoken.domain.service.ReturnTokenService;
import com.warehouse.returntoken.domain.vo.Token;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnTokenPortImpl implements ReturnTokenPort {

    private final ReturnTokenService returnTokenService;

    private final Logger log = LoggerFactory.getLogger("ReturnToken");

    @Override
    public Token determine(Parcel parcel) {
        log.info("Generating return token for parcel {}", parcel.getId());
        return returnTokenService.determineToken(parcel);
    }
}
