package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.supplier.domain.port.secondary.MailServicePort;
import com.warehouse.supplier.domain.vo.SupplierInvitation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailServiceAdapter implements MailServicePort {

    @Override
    public void sendInvitationMail(final SupplierInvitation invitation) {

    }
}
