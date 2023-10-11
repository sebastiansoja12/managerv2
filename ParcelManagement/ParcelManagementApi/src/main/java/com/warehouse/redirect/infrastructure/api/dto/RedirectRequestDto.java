package com.warehouse.redirect.infrastructure.api.dto;

import lombok.Data;

@Data
public class RedirectRequestDto {
    Long parcelId;
    String email;
}
