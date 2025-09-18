package com.warehouse.dangerousgood.domain.service;

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
        this.dangerousGoodRepository.create(dangerousGood);
        this.shipmentNotifierPort.notifyDangerousGoodCreated(dangerousGood);
    }

    @Override
    public DangerousGoodId nextDangerousGoodId() {
        return new DangerousGoodId(UUID.randomUUID().getLeastSignificantBits());
    }
}
