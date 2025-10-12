package com.warehouse.returning.infrastructure.adapter.primary.validator;

import java.util.List;

import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnRequestApi;

public abstract class RequestValidator {
    public abstract Result<Void, List<String>> validateBody(final ReturnRequestApi request);
}
