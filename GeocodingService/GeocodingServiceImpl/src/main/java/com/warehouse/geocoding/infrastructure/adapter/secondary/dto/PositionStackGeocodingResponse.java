package com.warehouse.geocoding.infrastructure.adapter.secondary.dto;

import java.util.List;

public record PositionStackGeocodingResponse(List<PositionStackGeocodingResult> data) {
}
