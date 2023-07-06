package com.warehouse.tsp.domain.service;

import com.warehouse.tsp.domain.model.Depot;

import java.util.List;

public interface ComputeService {
    String computeLength(List<Depot> depots);
}
