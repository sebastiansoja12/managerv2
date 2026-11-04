package com.warehouse.returning.application.port.primary;

import com.warehouse.returning.domain.vo.ReturnPackageId;

public interface ReturnReadModelSyncPort {

    void syncReadModel(final ReturnPackageId returnPackageId);
}
