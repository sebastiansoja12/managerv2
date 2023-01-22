package com.warehouse.depot.domain.port.primary;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.model.DepotId;
import com.warehouse.depot.domain.port.secondary.DepotSecondaryPort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DepotPortImpl implements DepotPort {


    private final DepotSecondaryPort depotSecondaryPort;

    @Override
    public void add(Depot depot) {
        depotSecondaryPort.add(depot);
    }

    @Override
    public Depot viewDepotById(DepotId depotId) {
        return depotSecondaryPort.viewById(depotId);
    }

    @Override
    public Depot viewDepotByCode(DepotCode depotCode) {
        return depotSecondaryPort.viewByCode(depotCode);
    }

    @Override
    public List<Depot> findAll() {
        return depotSecondaryPort.findAll();
    }

    @Override
    public void addMultipleDepots(List<Depot> depots) {
        depotSecondaryPort.addMultiple(depots);
    }
}
