package com.warehouse.returning.domain.port.primary;

import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnResponse;

public interface ReturnPort {
    ReturnResponse process(final ReturnRequest request);

    ReturnModel getReturn(ReturnId returnId);

    void delete(final ReturnPackageId returnPackageId);
}
