package com.warehouse.supplier.application.port.secondary;

import com.warehouse.supplier.domain.model.Supplier;

public interface MailServicePort {
    void notifyDriverLicenseInvalidated(final Supplier supplier);
}
