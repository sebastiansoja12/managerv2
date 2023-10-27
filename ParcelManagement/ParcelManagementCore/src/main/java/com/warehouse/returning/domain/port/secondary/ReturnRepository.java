package com.warehouse.returning.domain.port.secondary;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.ProcessReturn;

public interface ReturnRepository {

    ProcessReturn save(ReturnPackage returnPackage);
}
