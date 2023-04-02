package com.warehouse.depot.domain.port.primary;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.model.DepotId;
import com.warehouse.depot.domain.port.secondary.DepotRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DepotPortImpl implements DepotPort {


    private final DepotRepository depotRepository;

    @Override
    public void add(Depot depot) {
        depotRepository.save(depot);
    }

    @Override
    public Depot viewDepotById(DepotId depotId) {
        return depotRepository.viewById(depotId);
    }

    @Override
    public Depot viewDepotByCode(DepotCode depotCode) {
        return depotRepository.viewByCode(depotCode);
    }

    @Override
    public List<Depot> findAll() {
        return depotRepository.findAll();
    }

    @Override
    public void addMultipleDepots(List<Depot> depots) {
        depotRepository.saveAll(depots);
    }
}
