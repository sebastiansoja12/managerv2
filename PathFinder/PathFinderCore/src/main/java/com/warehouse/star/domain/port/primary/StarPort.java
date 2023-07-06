package com.warehouse.star.domain.port.primary;

import com.warehouse.star.domain.model.Coordinates;
import com.warehouse.star.domain.model.Depot;

import java.util.List;

public interface StarPort {
    String starPathFinder(String depotCode, List<Depot> depots, Coordinates coordinates);
}
