package com.warehouse.returning.domain.port.secondary;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;

public interface ReturnRepository {

    ProcessReturn save(ReturnPackage returnPackage);

    ProcessReturn update(ReturnPackage returnPackage);

    ReturnModel get(ReturnId returnId);

    void delete(ReturnId returnId);
}
