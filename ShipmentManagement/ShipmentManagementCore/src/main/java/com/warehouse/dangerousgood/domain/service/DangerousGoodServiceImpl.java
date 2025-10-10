package com.warehouse.dangerousgood.domain.service;

import com.warehouse.commonassets.model.Weight;
import com.warehouse.dangerousgood.domain.model.DangerousGood;
import com.warehouse.dangerousgood.domain.port.secondary.DangerousGoodRepository;
import com.warehouse.dangerousgood.domain.port.secondary.ShipmentNotifierPort;
import com.warehouse.dangerousgood.domain.vo.DangerousGoodId;

import java.util.UUID;

public class DangerousGoodServiceImpl implements DangerousGoodService {

    private final DangerousGoodRepository dangerousGoodRepository;

    private final ShipmentNotifierPort shipmentNotifierPort;

    public DangerousGoodServiceImpl(final DangerousGoodRepository dangerousGoodRepository,
                                    final ShipmentNotifierPort shipmentNotifierPort) {
        this.dangerousGoodRepository = dangerousGoodRepository;
        this.shipmentNotifierPort = shipmentNotifierPort;
    }

    @Override
    public void createDangerousGood(final DangerousGood dangerousGood) {
        this.dangerousGoodRepository.createOrUpdate(dangerousGood);
        this.shipmentNotifierPort.notifyDangerousGoodCreated(dangerousGood);
    }

    @Override
    public void updateWeight(final DangerousGoodId dangerousGoodId, final Weight weight) {
        final DangerousGood dangerousGood = this.dangerousGoodRepository.findById(dangerousGoodId);
        dangerousGood.changeWeight(weight);
        this.dangerousGoodRepository.createOrUpdate(dangerousGood);
    }

    @Override
    public DangerousGoodId nextDangerousGoodId() {
        final long value = UUID.randomUUID().getLeastSignificantBits();
        return new DangerousGoodId(Math.abs(value));
    }

    @Override
    public DangerousGood findById(final DangerousGoodId dangerousGoodId) {
        return this.dangerousGoodRepository.findById(dangerousGoodId);
    }
}
