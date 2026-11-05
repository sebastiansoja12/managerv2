package com.warehouse.star.domain.service;

import com.warehouse.star.domain.model.Depot;

public interface CalculateDistanceBetweenDepots {
      double calculateDistanceBetweenDepots(Depot depot1, Depot depot2);

      double calculateDistanceBetweenDepotsWithoutEarthCurve(Depot depot1, Depot depot2);
}
