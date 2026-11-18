package com.warehouse.organisationstructure.deliveryarea.infrastructure.adapter.primary.api;

import java.time.Instant;
import java.util.List;

public record DeliveryAreaApiResponse(Long deliveryAreaId,
                                      Long departmentId,
                                      String name,
                                      String source,
                                      List<GeoPointApi> points,
                                      Instant updatedAt) {
}
