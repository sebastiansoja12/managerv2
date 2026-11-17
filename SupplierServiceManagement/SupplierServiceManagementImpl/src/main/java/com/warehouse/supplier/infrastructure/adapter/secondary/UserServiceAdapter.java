package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.auth.SupplierUserEventPublisher;
import com.warehouse.auth.event.SupplierCreatedEvent;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.application.port.secondary.UserServicePort;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.OutputRequestMapper;

import java.util.concurrent.atomic.AtomicReference;

public class UserServiceAdapter implements UserServicePort {

    private final SupplierUserEventPublisher supplierUserEventPublisher;

    public UserServiceAdapter(final SupplierUserEventPublisher supplierUserEventPublisher) {
        this.supplierUserEventPublisher = supplierUserEventPublisher;
    }

    @Override
    public UserId createUserForSupplier(final SupplierSnapshot snapshot) {
        final AtomicReference<UserId> userCreatedId = new AtomicReference<>();
        final SupplierCreatedEvent event = OutputRequestMapper.map(snapshot, userCreatedId::set);
        this.supplierUserEventPublisher.sendEvent(event);
        final UserId userId = userCreatedId.get();
        if (userId == null) {
            throw new IllegalStateException("Supplier user was not created");
        }
        return userId;
    }
}
