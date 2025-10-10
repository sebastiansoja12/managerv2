package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@SuperBuilder
@Jacksonized
public class UsernameApi {
    String value;
}
