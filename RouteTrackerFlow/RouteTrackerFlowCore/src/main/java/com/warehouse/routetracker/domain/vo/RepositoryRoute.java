package com.warehouse.routetracker.domain.vo;

import com.warehouse.routetracker.domain.model.Route;
import lombok.Value;


@Value
public class RepositoryRoute {
    Route route;
    String id;
    String status;
    String failureReason;
}
