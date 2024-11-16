package com.warehouse.depot.domain.port.primary;

import java.util.List;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.vo.DepotCode;
import com.warehouse.depot.domain.vo.UpdateStreetRequest;

public interface DepotPort {

    Depot viewDepotByCode(DepotCode depotCode);

    List<Depot> findAll();

    void addMultipleDepots(List<Depot> depots);

    void updateStreet(UpdateStreetRequest request);
}
