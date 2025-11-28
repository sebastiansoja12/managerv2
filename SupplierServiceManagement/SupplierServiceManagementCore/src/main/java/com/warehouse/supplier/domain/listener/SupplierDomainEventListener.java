package com.warehouse.supplier.domain.listener;

import com.warehouse.supplier.domain.event.SupplierCreated;
import com.warehouse.supplier.domain.port.secondary.MailServicePort;
import com.warehouse.supplier.domain.vo.SupplierSnapshot;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SupplierDomainEventListener {

    private final MailServicePort mailServicePort;

    public SupplierDomainEventListener(final MailServicePort mailServicePort) {
        this.mailServicePort = mailServicePort;
    }

    @EventListener
    public void handle(final SupplierCreated event) {
        final SupplierSnapshot snapshot = event.getSnapshot();

    }
}
