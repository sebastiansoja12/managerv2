package com.warehouse.supplier.domain.port.secondary;

import com.warehouse.supplier.domain.vo.SupplierInvitation;

public interface MailServicePort {
    void sendInvitationMail(final SupplierInvitation invitation);
}
