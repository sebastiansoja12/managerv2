package com.warehouse.star;

import com.warehouse.dto.CoordinatesDto;
import com.warehouse.dto.DepotDto;

import java.util.List;

public interface StarService {
    String starPathFinder(String depotCode, List<DepotDto> depotsRequestList, CoordinatesDto coordinates);
}
