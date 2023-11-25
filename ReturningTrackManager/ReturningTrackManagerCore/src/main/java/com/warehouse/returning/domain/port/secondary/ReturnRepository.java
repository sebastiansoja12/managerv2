package com.warehouse.returning.domain.port.secondary;

import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;

public interface ReturnRepository {

    ProcessReturn save(ReturnPackageRequest returnPackage);

    ProcessReturn update(ReturnPackageRequest returnPackage);

    ReturnModel get(ReturnId returnId);

    ReturnStatus unlockReturn(Long parcelId, String returnToken);

    void delete(ReturnId returnId);
}
