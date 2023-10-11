package com.warehouse.voronoi.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Coordinates {

    double lat;
    double lon;
}
