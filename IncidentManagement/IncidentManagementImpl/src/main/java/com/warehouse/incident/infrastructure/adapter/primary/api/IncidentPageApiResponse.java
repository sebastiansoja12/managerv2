package com.warehouse.incident.infrastructure.adapter.primary.api;

import java.util.List;

public record IncidentPageApiResponse(List<IncidentApiResponse> incidents,
                                      int page,
                                      int size,
                                      long totalElements) {
}
