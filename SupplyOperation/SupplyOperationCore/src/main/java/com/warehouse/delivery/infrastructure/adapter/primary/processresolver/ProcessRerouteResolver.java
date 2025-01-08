package com.warehouse.delivery.infrastructure.adapter.primary.processresolver;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;
import com.warehouse.delivery.infrastructure.adapter.primary.ProcessHandler;
import org.springframework.stereotype.Component;

@Component
public class ProcessRerouteResolver implements ProcessHandler {

    @Override
    public boolean supports(final ProcessType processType) {
        return false;
    }

    @Override
    public Response processRequest(final Request request) {
        return null;
    }
}
