package com.warehouse.returning.domain.port.primary;

import java.util.Optional;

import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.vo.*;

public interface ReturnPort {
    ReturnResponse process(final ReturnRequest request);

    void changeReasonCode(final ChangeReasonCodeRequest request);

    void startProcessing(final ReturnPackageId returnPackageId);

    void complete(final ReturnPackageId returnPackageId);

    ReturnPackage getReturn(final ReturnPackageId returnId);

    Optional<ReturnPackage> findLatestReturn(final ShipmentId shipmentId, final OperatorId operatorId);

    ReturnPage getReturns(final DepartmentId departmentId, final OperatorId operatorId, final int page, final int size);

    ReturnTokenValidation validateReturnToken(final ShipmentId shipmentId, final ReturnToken returnToken);

    void delete(final ReturnPackageId returnPackageId);
}
