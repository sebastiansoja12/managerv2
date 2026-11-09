package com.warehouse.returning.application.port.primary;

import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.domain.vo.ReturnPage;

public interface ReturnReadModelQueryPort {

    ReturnPage getReturns(final DepartmentId departmentId, final OperatorId operatorId, final int page,
                          final int size);
}
