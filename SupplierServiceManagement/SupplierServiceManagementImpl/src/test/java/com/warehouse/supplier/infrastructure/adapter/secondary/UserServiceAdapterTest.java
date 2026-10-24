package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.auth.SupplierUserEventPublisher;
import com.warehouse.auth.event.SupplierCreatedEvent;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.domain.model.Supplier;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceAdapterTest {

    @Test
    void shouldReturnUserIdCreatedByAuthorization() {
        final AtomicReference<SupplierCreatedEvent> publishedEvent = new AtomicReference<>();
        final SupplierUserEventPublisher publisher = event -> {
            publishedEvent.set(event);
            event.getUserCreatedId().accept(new UserId(101L));
        };
        final UserServiceAdapter adapter = new UserServiceAdapter(publisher);

        final UserId userId = adapter.createUserForSupplier(supplier().snapshot());

        assertThat(userId).isEqualTo(new UserId(101L));
        assertThat(publishedEvent.get().getSupplierId()).isEqualTo(new SupplierId(11L));
        assertThat(publishedEvent.get().getSupplierCode()).isEqualTo(new SupplierCode("KUR-01"));
        assertThat(publishedEvent.get().getEmail()).isEqualTo("adam@example.com");
        assertThat(publishedEvent.get().getDepartmentId()).isEqualTo(new DepartmentId(3L));
    }

    @Test
    void shouldThrowExceptionWhenUserWasNotCreated() {
        final UserServiceAdapter adapter = new UserServiceAdapter(event -> {
        });

        assertThrows(IllegalStateException.class, () -> adapter.createUserForSupplier(supplier().snapshot()));
    }

    private Supplier supplier() {
        return new Supplier(new SupplierId(11L), new SupplierCode("KUR-01"), "Adam", "Nowak", "+48111222333",
                "adam@example.com", new DepartmentId(3L));
    }
}
