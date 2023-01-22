package com.warehouse.depot.api;

import com.warehouse.depot.api.dto.DepotCodeDto;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.depot.api.dto.DepotIdDto;

import java.util.List;

public interface DepotService {

    void add(DepotDto depot);

    void addMultipleDepots(List<DepotDto> depot);

    DepotDto viewDepotById(DepotIdDto depotIdDto);

    DepotDto viewDepotByCode(DepotCodeDto depotCodeDto);

    List<DepotDto> findAll();
}
