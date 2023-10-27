package com.warehouse.depot.domain.port.primary;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.port.secondary.DepotRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DepotPortImpl implements DepotPort {


    private final DepotRepository depotRepository;

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
