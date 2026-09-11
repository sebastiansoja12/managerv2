package com.warehouse.account.infrastructure.adapter.primary;

import com.warehouse.account.application.port.primary.SupplierAccountPort;
import com.warehouse.account.application.port.primary.command.ProvisionSupplierAccountCommand;
import com.warehouse.auth.event.SupplierCreatedEvent;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SupplierAccountEventListener {

    private final SupplierAccountPort supplierAccountPort;

    public SupplierAccountEventListener(final SupplierAccountPort supplierAccountPort) {
        this.supplierAccountPort = supplierAccountPort;
    }

    @EventListener
    public void handle(final SupplierCreatedEvent event) {
        final UserId userId = this.supplierAccountPort.provisionSupplierAccount(new ProvisionSupplierAccountCommand(
                event.getSupplierId(),
                event.getSupplierCode(),
                event.getFirstName(),
                event.getLastName(),
                event.getEmail(),
                event.getDepartmentId()));
        event.getUserCreatedId().accept(userId);
    }
}
