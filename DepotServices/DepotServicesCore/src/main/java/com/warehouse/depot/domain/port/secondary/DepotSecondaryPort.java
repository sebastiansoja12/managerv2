package com.warehouse.depot.domain.port.secondary;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.model.DepotId;

public interface DepotSecondaryPort {

    void add(Depot depot);

    Depot viewByCode(DepotCode depotCode);

    Depot viewById(DepotId depotId);
}
