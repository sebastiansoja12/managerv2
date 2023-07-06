package com.warehouse.voronoi.domain.service;

import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Depot;

import java.util.List;

public interface ComputeService {

    String computeLength(Coordinates coordinates, List<Depot> depots);
}
