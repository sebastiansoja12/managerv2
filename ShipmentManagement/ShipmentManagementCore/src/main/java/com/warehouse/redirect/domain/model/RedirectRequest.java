package com.warehouse.redirect.domain.model;

import lombok.Data;

@Data
public class RedirectRequest {
    private Long parcelId;
    private String email;
}
