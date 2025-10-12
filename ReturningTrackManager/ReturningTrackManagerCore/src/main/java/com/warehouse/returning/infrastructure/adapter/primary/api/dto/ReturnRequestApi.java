package com.warehouse.returning.infrastructure.adapter.primary.api.dto;


import java.util.List;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record ReturnRequestApi(
        List<ReturnPackageRequestApi> requests
) {
    @Override
    @NonNull
    public String toString() {
        return "ReturnRequestApi{" +
                "requests=" + requests.toString() +
                '}';
    }
}
