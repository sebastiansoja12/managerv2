package com.warehouse.returning.domain.vo;

import java.util.List;


public record ReturnResponse(List<ProcessReturn> processReturn) {
    @Override
    public List<ProcessReturn> processReturn() {
        return processReturn;
    }
}
