package com.warehouse.department.domain.port.primary;

import java.util.List;

import com.warehouse.department.domain.model.Depot;
import com.warehouse.department.domain.vo.DepotCode;
import com.warehouse.department.domain.vo.UpdateStreetRequest;

public interface DepotPort {

    Depot viewDepotByCode(DepotCode depotCode);

    List<Depot> findAll();

    void addMultipleDepots(List<Depot> depots);

    void updateStreet(UpdateStreetRequest request);
}
