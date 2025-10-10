package com.warehouse.dangerousgood.domain.port.secondary;

import com.warehouse.dangerousgood.domain.model.DangerousGood;

public interface ShipmentNotifierPort {
    void notifyDangerousGoodCreated(final DangerousGood dangerousGood);
}
