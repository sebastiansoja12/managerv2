package com.warehouse.depot.domain.port.secondary;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.vo.DepotCode;

import java.util.List;

public interface DepotRepository {

    void save(Depot depot);

    void saveAll(List<Depot> depots);

    Depot findByCode(DepotCode depotCode);

    List<Depot> findAll();

}
