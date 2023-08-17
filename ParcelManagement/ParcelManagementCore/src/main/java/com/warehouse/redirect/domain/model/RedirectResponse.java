package com.warehouse.redirect.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RedirectResponse {
    String token;
    Long parcelId;
}
