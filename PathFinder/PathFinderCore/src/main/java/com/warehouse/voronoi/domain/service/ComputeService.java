package com.warehouse.voronoi.domain.service;

import java.util.List;

import com.warehouse.voronoi.domain.model.Depot;

public interface ComputeService {

    String calculate(String requestCity, List<Depot> depots);
}
