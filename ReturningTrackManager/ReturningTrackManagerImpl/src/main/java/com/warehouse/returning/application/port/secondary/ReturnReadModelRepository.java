package com.warehouse.returning.application.port.secondary;

import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;
import com.warehouse.returning.domain.vo.ReturnPage;

public interface ReturnReadModelRepository {

    void sync(final ReturnPackageSnapshot snapshot);

    ReturnPage findByDepartmentIdAndOperatorId(final DepartmentId departmentId, final OperatorId operatorId,
                                               final int page, final int size);
}
