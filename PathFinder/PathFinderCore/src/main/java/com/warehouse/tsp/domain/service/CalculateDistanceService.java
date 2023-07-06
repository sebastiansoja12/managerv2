package com.warehouse.tsp.domain.service;

import com.warehouse.tsp.domain.model.Depot;

import java.util.List;

public interface CalculateDistanceService {

    double calculateDistance(List<Integer> route, List<Depot> depots);
}
