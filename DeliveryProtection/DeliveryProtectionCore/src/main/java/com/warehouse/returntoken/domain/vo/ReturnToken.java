package com.warehouse.returntoken.domain.vo;

import com.warehouse.returntoken.infrastructure.adapter.secondary.entity.ReturnTokenEntity;
import org.apache.commons.lang3.StringUtils;

public class ReturnToken {
    private final String value;

    public ReturnToken(final String value) {
        this.value = value;
    }

    public static ReturnToken empty() {
        return new ReturnToken(StringUtils.EMPTY);
    }

    public String getValue() {
        return value;
    }

    public static ReturnToken from(final ReturnTokenEntity returnTokenEntity) {
        return new ReturnToken(returnTokenEntity.getToken());
    }
}
