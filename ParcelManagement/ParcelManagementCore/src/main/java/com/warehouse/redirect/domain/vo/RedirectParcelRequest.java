package com.warehouse.redirect.domain.vo;

import lombok.Value;

@Value
public class RedirectParcelRequest {
    Long parcelId;
    RedirectParcel parcel;
    Token token;
}
