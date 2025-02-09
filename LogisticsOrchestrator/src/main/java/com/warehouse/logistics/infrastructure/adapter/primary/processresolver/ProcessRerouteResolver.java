package com.warehouse.logistics.infrastructure.adapter.primary.processresolver;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.logistics.domain.model.Response;
import com.warehouse.logistics.infrastructure.adapter.primary.ProcessHandler;
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
