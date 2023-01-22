package com.warehouse.depot.infrastructure.secondary;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.model.DepotId;
import com.warehouse.depot.domain.port.secondary.DepotRepository;
import com.warehouse.depot.domain.port.secondary.DepotSecondaryPort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DepotAdapter implements DepotSecondaryPort {

    private final DepotRepository depotRepository;

    @Override
    public void add(Depot depot) {
        depotRepository.save(depot);
    }

    @Override
    public Depot viewByCode(DepotCode depotCode) {
        return depotRepository.viewByCode(depotCode);
    }

    @Override
    public Depot viewById(DepotId depotId) {
        return depotRepository.viewById(depotId);
    }

    @Override
    public List<Depot> findAll() {
        return depotRepository.findAll();
    }

    @Override
    public void addMultiple(List<Depot> depots) {
        depotRepository.saveAll(depots);
    }
}
