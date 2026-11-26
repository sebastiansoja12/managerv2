package com.warehouse.supplier.domain.event;

import com.warehouse.eventoutbox.domain.model.DomainEvent;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;

public interface SupplierEvent extends DomainEvent {

    SupplierSnapshot getSnapshot();
}
