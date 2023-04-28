package com.warehouse.star;

import com.warehouse.depot.api.dto.CoordinatesDto;
import com.warehouse.depot.api.dto.DepotDto;

import java.util.List;

public interface StarService {
    String starPathFinder(String depotCode, List<DepotDto> depotsRequestList, CoordinatesDto coordinatesDto);
}
