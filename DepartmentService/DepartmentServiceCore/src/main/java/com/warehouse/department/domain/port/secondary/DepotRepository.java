package com.warehouse.department.domain.port.secondary;

import com.warehouse.department.domain.model.Depot;
import com.warehouse.department.domain.vo.DepotCode;

import java.util.List;

public interface DepotRepository {

    void save(Depot depot);

    void saveAll(List<Depot> depots);

    Depot findByCode(DepotCode depotCode);

    List<Depot> findAll();

}
