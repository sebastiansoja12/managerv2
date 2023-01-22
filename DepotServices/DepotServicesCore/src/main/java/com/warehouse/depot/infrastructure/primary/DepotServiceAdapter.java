package com.warehouse.depot.infrastructure.primary;

import com.warehouse.depot.api.DepotService;
import com.warehouse.depot.api.dto.DepotCodeDto;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.depot.api.dto.DepotIdDto;
import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.model.DepotId;
import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.depot.infrastructure.primary.mapper.DepotRequestMapper;
import com.warehouse.depot.infrastructure.primary.mapper.DepotResponseMapper;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DepotServiceAdapter implements DepotService {

    private final DepotRequestMapper requestMapper;

    private final DepotResponseMapper responseMapper;

    private final DepotPort depotPort;

    @Override
    public void add(DepotDto depotDto) {
        final Depot depot = requestMapper.map(depotDto);
        depotPort.add(depot);
    }

    @Override
    public void addMultipleDepots(List<DepotDto> depot) {
        final List<Depot> depots = requestMapper.map(depot);
        depotPort.addMultipleDepots(depots);
    }

    @Override
    public DepotDto viewDepotById(DepotIdDto depotIdDto) {
        final DepotId depotId = requestMapper.map(depotIdDto);
        final Depot depot = depotPort.viewDepotById(depotId);
        return responseMapper.map(depot);
    }

    @Override
    public DepotDto viewDepotByCode(DepotCodeDto depotCodeDto) {
        final DepotCode depotCode = requestMapper.map(depotCodeDto);
        final Depot depot = depotPort.viewDepotByCode(depotCode);
        return responseMapper.map(depot);
    }

    @Override
    public List<DepotDto> findAll() {
        return responseMapper.map(depotPort.findAll());
    }
}
