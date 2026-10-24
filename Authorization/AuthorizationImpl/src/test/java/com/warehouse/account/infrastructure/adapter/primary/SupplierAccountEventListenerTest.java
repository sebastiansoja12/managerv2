package com.warehouse.account.infrastructure.adapter.primary;

import com.warehouse.account.application.port.primary.SupplierAccountPort;
import com.warehouse.account.application.port.primary.command.ProvisionSupplierAccountCommand;
import com.warehouse.auth.event.SupplierCreatedEvent;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SupplierAccountEventListenerTest {

    private final SupplierAccountPort supplierAccountPort = mock(SupplierAccountPort.class);

    private final SupplierAccountEventListener listener = new SupplierAccountEventListener(supplierAccountPort);

    @Test
    void shouldProvisionAccountAndReturnUserIdToCaller() {
        final AtomicReference<UserId> createdUserId = new AtomicReference<>();
        when(supplierAccountPort.provisionSupplierAccount(new ProvisionSupplierAccountCommand(new SupplierId(11L),
                new SupplierCode("KUR-01"), "Adam", "Nowak", "adam@example.com", new DepartmentId(3L))))
                .thenReturn(new UserId(101L));

        listener.handle(new SupplierCreatedEvent(new SupplierId(11L), new SupplierCode("KUR-01"), "Adam", "Nowak",
                "adam@example.com", new DepartmentId(3L), createdUserId::set));

        assertEquals(new UserId(101L), createdUserId.get());
    }
}
