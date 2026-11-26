package com.warehouse.supplier.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;

public interface DangerousGoodObserverPort {
    void run();

    int run(final OperatorId operatorId);
}
