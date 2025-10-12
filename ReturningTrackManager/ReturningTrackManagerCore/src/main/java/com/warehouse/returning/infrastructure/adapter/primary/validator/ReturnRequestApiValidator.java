package com.warehouse.returning.infrastructure.adapter.primary.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnRequestApi;

@Component
public class ReturnRequestApiValidator extends RequestValidator {

    @Override
    public Result<Void, List<String>> validateBody(final ReturnRequestApi request) {
        return Result.success();
    }
}
