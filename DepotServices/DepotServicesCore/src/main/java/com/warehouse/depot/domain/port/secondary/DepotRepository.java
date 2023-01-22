package com.warehouse.depot.domain.port.secondary;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.model.DepotId;

import java.util.List;

public interface DepotRepository {
    void save(Depot depot);

    Depot viewByCode(DepotCode depotCode);

    Depot viewById(DepotId depotId);

    List<Depot> findAll();

    void saveAll(List<Depot> depots);
}
