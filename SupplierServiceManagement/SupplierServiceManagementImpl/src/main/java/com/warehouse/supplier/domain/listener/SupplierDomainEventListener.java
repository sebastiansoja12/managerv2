package com.warehouse.supplier.domain.listener;

import com.warehouse.commonassets.identificator.UserId;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.warehouse.supplier.domain.event.SupplierUpdated;
import com.warehouse.supplier.domain.port.secondary.MailServicePort;
import com.warehouse.supplier.domain.port.secondary.UserServicePort;
import com.warehouse.supplier.domain.service.SupplierService;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;

@Component
public class SupplierDomainEventListener {

    private final MailServicePort mailServicePort;

    private final SupplierService supplierService;

    private final UserServicePort userServicePort;

    public SupplierDomainEventListener(final MailServicePort mailServicePort,
                                       final SupplierService supplierService,
                                       final UserServicePort userServicePort) {
        this.mailServicePort = mailServicePort;
        this.supplierService = supplierService;
        this.userServicePort = userServicePort;
    }

    @EventListener
    public void handle(final SupplierUpdated event) {
        final SupplierSnapshot snapshot = event.getSnapshot();
        final UserId userId = this.userServicePort.createUserForSupplier(snapshot);
        this.supplierService.updateUserCreated(snapshot.supplierId(), userId);
    }
}
