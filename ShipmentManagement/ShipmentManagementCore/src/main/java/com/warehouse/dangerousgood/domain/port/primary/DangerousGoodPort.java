package com.warehouse.dangerousgood.domain.port.primary;


import com.warehouse.dangerousgood.domain.model.DangerousGoodCreateRequest;

public interface DangerousGoodPort {
    void createDangerousGood(final DangerousGoodCreateRequest request);
}
