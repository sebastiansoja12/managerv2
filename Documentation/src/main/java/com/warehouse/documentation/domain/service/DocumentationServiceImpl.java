package com.warehouse.documentation.domain.service;

import com.warehouse.documentation.domain.model.DocumentationRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DocumentationServiceImpl implements DocumentationService {

    @Override
    public void generate(HttpServletResponse response, DocumentationRequest request) {

    }
}
