package com.warehouse.tsp.domain.service;

import com.warehouse.tsp.domain.model.Depot;

public interface CalculateDistanceBetweenDepots {
      double calculateDistanceBetweenDepots(Depot depot1, Depot depot2);

      double calculateDistanceBetweenDepotsWithoutEarthCurve(Depot depot1, Depot depot2);
}
