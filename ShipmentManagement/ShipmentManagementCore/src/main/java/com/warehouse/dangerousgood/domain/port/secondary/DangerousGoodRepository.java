package com.warehouse.dangerousgood.domain.port.secondary;

import com.warehouse.dangerousgood.domain.model.DangerousGood;

public interface DangerousGoodRepository {
    void create(final DangerousGood dangerousGood);
}
