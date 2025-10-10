package com.warehouse.returning.domain.port.primary;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.vo.*;

public interface ReturnPort {
    ReturnResponse process(final ReturnRequest request);

    void changeReasonCode(final ChangeReasonCodeRequest request);

    ReturnPackage getReturn(final ReturnPackageId returnId);

    void delete(final ReturnPackageId returnPackageId);
}
