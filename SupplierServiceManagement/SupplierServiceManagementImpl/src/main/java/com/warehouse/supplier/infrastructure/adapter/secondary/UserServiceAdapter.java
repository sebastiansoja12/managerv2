package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.auth.SupplierUserEventPublisher;
import com.warehouse.auth.event.SupplierCreatedEvent;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.domain.port.secondary.UserServicePort;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;

import java.util.concurrent.atomic.AtomicReference;

public class UserServiceAdapter implements UserServicePort {

    private final SupplierUserEventPublisher supplierUserEventPublisher;

    public UserServiceAdapter(final SupplierUserEventPublisher supplierUserEventPublisher) {
        this.supplierUserEventPublisher = supplierUserEventPublisher;
    }

    @Override
    public UserId createUserForSupplier(final SupplierSnapshot snapshot) {
        final AtomicReference<UserId> userCreatedId = new AtomicReference<>();
        this.supplierUserEventPublisher.sendEvent(new SupplierCreatedEvent(
                snapshot.supplierId(),
                snapshot.supplierCode(),
                snapshot.firstName(),
                snapshot.lastName(),
                snapshot.email(),
                snapshot.departmentId(),
                userCreatedId::set));
        final UserId userId = userCreatedId.get();
        if (userId == null) {
            throw new IllegalStateException("Supplier user was not created");
        }
        return userId;
    }
}
