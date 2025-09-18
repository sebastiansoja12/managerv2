package com.warehouse.dangerousgood.domain.service;

import com.warehouse.dangerousgood.domain.model.DangerousGood;
import com.warehouse.dangerousgood.domain.vo.DangerousGoodId;

public interface DangerousGoodService {
    void createDangerousGood(final DangerousGood dangerousGood);
    DangerousGoodId nextDangerousGoodId();
}
