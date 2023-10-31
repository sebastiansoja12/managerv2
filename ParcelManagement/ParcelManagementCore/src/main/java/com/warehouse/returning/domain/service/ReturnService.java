package com.warehouse.returning.domain.service;

import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.vo.ReturnResponse;

public interface ReturnService {
    ReturnResponse processReturning(ReturnRequest request);

    ReturnResponse updateReturning(ReturnRequest request);
}
