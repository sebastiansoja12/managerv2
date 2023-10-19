package com.warehouse.documentation.domain.port.primary;

import com.warehouse.documentation.domain.model.DocumentationRequest;

import jakarta.servlet.http.HttpServletResponse;

public interface DocumentationPort {
    void documentationOnDemand(HttpServletResponse response, DocumentationRequest request);
}
