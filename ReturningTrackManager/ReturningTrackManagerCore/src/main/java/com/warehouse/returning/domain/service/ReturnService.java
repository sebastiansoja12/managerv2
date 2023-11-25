package com.warehouse.returning.domain.service;

import java.util.List;

import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;

public interface ReturnService {
    List<ProcessReturn> processReturn(ReturnRequest request);

    List<ProcessReturn> updateReturn(ReturnRequest request);

    ReturnPackageRequest unlockReturn(ReturnPackageRequest request);

    ReturnModel getReturn(ReturnId returnId);

    void deleteReturn(ReturnId returnId);
}
