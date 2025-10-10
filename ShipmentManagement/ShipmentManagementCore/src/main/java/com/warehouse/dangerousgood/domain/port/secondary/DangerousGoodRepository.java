package com.warehouse.dangerousgood.domain.port.secondary;

import com.warehouse.dangerousgood.domain.model.DangerousGood;
import com.warehouse.dangerousgood.domain.vo.DangerousGoodId;

public interface DangerousGoodRepository {
    void createOrUpdate(final DangerousGood dangerousGood);
    DangerousGood findById(final DangerousGoodId dangerousGoodId);
}
