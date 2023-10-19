package com.warehouse.documentation.domain.service;

import com.warehouse.documentation.domain.model.DocumentationRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface DocumentationService {
    void generate(HttpServletResponse response, DocumentationRequest request);
}

