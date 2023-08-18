package com.warehouse.redirect.infrastructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedirectResponseDto {
    TokenDto token;
    Long parcelId;
}
