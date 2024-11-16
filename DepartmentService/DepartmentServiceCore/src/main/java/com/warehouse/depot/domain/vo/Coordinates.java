package com.warehouse.depot.domain.vo;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class Coordinates {
    double lat;
    double lon;
}
