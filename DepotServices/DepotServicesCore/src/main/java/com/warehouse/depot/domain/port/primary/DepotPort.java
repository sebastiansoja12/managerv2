package com.warehouse.depot.domain.port.primary;

import com.warehouse.depot.domain.vo.Depot;
import com.warehouse.depot.domain.vo.DepotCode;

import java.util.List;

public interface DepotPort {

    Depot viewDepotByCode(DepotCode depotCode);

    List<Depot> findAll();

    void addMultipleDepots(List<Depot> depots);
}
