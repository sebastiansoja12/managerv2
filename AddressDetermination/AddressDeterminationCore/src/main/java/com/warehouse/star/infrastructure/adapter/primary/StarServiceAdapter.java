package com.warehouse.star.infrastructure.adapter.primary;

import com.warehouse.depot.api.dto.CoordinatesDto;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.star.StarService;
import com.warehouse.star.domain.model.Coordinates;
import com.warehouse.star.domain.model.Depot;
import com.warehouse.star.domain.port.primary.StarPort;
import com.warehouse.star.infrastructure.adapter.primary.mapper.StarRequestMapper;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class StarServiceAdapter implements StarService {

    private final StarPort starPort;

    private final StarRequestMapper starRequestMapper;

    @Override
    public String starPathFinder(String depotCode, List<DepotDto> depotsRequestList, CoordinatesDto coordinatesDto) {
        final List<Depot> depots = starRequestMapper.map(depotsRequestList);
        final Coordinates coordinates = starRequestMapper.map(coordinatesDto);
        return starPort.starPathFinder(depotCode, depots, coordinates);
    }
}



