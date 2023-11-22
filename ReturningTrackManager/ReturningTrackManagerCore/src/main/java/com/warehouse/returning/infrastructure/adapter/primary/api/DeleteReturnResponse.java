package com.warehouse.returning.infrastructure.adapter.primary.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
public class DeleteReturnResponse {
    ResponseStatus responseStatus;
}
