package com.warehouse.redirect.domain.model;

import lombok.Data;

@Data
public class RedirectRequest {
    Long parcelId;
    String email;
}
