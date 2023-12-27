package com.warehouse.depot.domain.port.secondary;

import com.warehouse.depot.domain.vo.Depot;
import com.warehouse.depot.domain.vo.DepotCode;

import java.util.List;

public interface DepotRepository {

    void saveAll(List<Depot> depots);

    Depot viewByCode(DepotCode depotCode);

    List<Depot> findAll();

}
