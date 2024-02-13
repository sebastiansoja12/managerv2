package com.warehouse.redirect.domain.vo;

import lombok.Value;


@Value
public class RedirectToken {

    String token;

    Long parcelId;

    String email;
}
