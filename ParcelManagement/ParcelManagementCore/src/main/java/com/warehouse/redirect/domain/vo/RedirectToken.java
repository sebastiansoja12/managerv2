package com.warehouse.redirect.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class RedirectToken {

    String token;

    Long parcelId;

    String email;

    public RedirectToken(String token, Long parcelId, String email) {
        this.token = token;
        this.parcelId = parcelId;
        this.email = email;
    }
}
