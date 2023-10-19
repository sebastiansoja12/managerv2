package com.warehouse.documentation.domain.port.primary;

import com.warehouse.documentation.domain.model.DocumentationRequest;

import com.warehouse.documentation.domain.service.DocumentationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@AllArgsConstructor
@Slf4j
public class DocumentationPortImpl implements DocumentationPort {

    private final DocumentationService documentationService;

    private final String DOC_NAME = "%s_history_%s%s";

    private final String PDF = ".pdf";

    private final String HEADER_KEY = "Content-Disposition";

    private final String HEADER_VALUE = "attachment; filename=%s";


    @Override
    public void documentationOnDemand(HttpServletResponse response, DocumentationRequest request) {
        logDocumentationOnDemand(request);
        final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        final String currentDateTime = dateFormatter.format(new java.util.Date());
        final String fileName = String.format(DOC_NAME, request.getParcel().getId().toString(), currentDateTime, PDF);
        final String headerValue = String.format(HEADER_VALUE, fileName);
        response.setHeader(HEADER_KEY, headerValue);
        documentationService.generate(response, request);
    }

    private void logDocumentationOnDemand(DocumentationRequest request) {
        log.info("Requested documentation for parcel {}...", request.getParcel().getId());
    }
}
