package com.warehouse.reroute.infrastructure.adapter.primary.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RerouteShipmentApiRequest(@NotBlank String token,
                                        @NotNull @Valid ReroutePartyApi sender,
                                        @NotNull @Valid ReroutePartyApi recipient) {
}
