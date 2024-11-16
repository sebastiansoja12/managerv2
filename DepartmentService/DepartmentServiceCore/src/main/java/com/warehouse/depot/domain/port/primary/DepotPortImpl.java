package com.warehouse.depot.domain.port.primary;

import java.util.List;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.port.secondary.DepotRepository;
import com.warehouse.depot.domain.vo.DepotCode;
import com.warehouse.depot.domain.vo.UpdateStreetRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DepotPortImpl implements DepotPort {


    private final DepotRepository depotRepository;

    @Override
    public Depot viewDepotByCode(DepotCode depotCode) {
        return depotRepository.findByCode(depotCode);
    }

    @Override
    public List<Depot> findAll() {
        return depotRepository.findAll();
    }

    @Override
    public void addMultipleDepots(List<Depot> depots) {
        depotRepository.saveAll(depots);
    }

    @Override
    public void updateStreet(UpdateStreetRequest request) {
        final Depot depot = depotRepository.findByCode(request.depotCode());
        depot.updateStreet(request.street());
        depotRepository.save(depot);
    }
}
