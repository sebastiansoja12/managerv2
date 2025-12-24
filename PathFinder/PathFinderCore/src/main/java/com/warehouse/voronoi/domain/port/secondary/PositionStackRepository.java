package com.warehouse.voronoi.domain.port.secondary;

import com.warehouse.voronoi.domain.model.PositionStack;

public interface PositionStackRepository {
    PositionStack findPositionStackConfiguration();
}
