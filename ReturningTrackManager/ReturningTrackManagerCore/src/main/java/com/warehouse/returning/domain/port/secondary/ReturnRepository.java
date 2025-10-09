package com.warehouse.returning.domain.port.secondary;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import com.warehouse.returning.domain.vo.ReturnPackageId;

public interface ReturnRepository {

    ProcessReturn save(ReturnPackageRequest returnPackage);

    ProcessReturn update(ReturnPackageRequest returnPackage);

    ReturnPackage findById(final ReturnPackageId returnPackageId);

    ReturnStatus unlockReturn(Long parcelId, String returnToken);

    void createOrUpdate(final ReturnPackage returnPackage);

    void delete(ReturnId returnId);
}
