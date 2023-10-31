package com.warehouse.returning.domain.service;

import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;
import com.warehouse.returning.domain.vo.ReturnResponse;

public interface ReturnService {
    ReturnResponse processReturn(ReturnRequest request);

    ReturnResponse updateReturn(ReturnRequest request);

    ReturnModel getReturn(ReturnId returnId);

    void deleteReturn(ReturnId returnId);
}
