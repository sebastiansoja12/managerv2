package com.warehouse.dangerousgood.domain.service;

import com.warehouse.commonassets.model.Weight;
import com.warehouse.dangerousgood.domain.model.DangerousGood;
import com.warehouse.dangerousgood.domain.vo.DangerousGoodId;

public interface DangerousGoodService {
    void createDangerousGood(final DangerousGood dangerousGood);
    void updateWeight(final DangerousGoodId dangerousGoodId, final Weight weight);
    DangerousGoodId nextDangerousGoodId();
    DangerousGood findById(final DangerousGoodId dangerousGoodId);
}
