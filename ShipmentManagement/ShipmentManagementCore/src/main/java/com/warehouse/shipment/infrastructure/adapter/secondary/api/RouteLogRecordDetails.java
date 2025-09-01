package com.warehouse.shipment.infrastructure.adapter.secondary.api;

import java.util.Set;

import lombok.Builder;

@Builder
public record RouteLogRecordDetails(
        Set<RouteLogRecordDetail> routeLogRecordDetailSet
) {}