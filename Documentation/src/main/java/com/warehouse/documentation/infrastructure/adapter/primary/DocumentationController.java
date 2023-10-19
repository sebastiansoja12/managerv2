package com.warehouse.documentation.infrastructure.adapter.primary;

import com.warehouse.documentation.domain.model.DocumentationRequest;
import com.warehouse.documentation.domain.port.primary.DocumentationPort;
import com.warehouse.documentation.infrastructure.adapter.primary.api.dto.DocumentationRequestDto;
import com.warehouse.documentation.infrastructure.adapter.primary.mapper.DocumentationRequestMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.mapstruct.factory.Mappers.getMapper;

@RestController
@RequestMapping("/documentations")
@RequiredArgsConstructor
public class DocumentationController {


    private final DocumentationPort documentationPort;

    private final DocumentationRequestMapper requestMapper = getMapper(DocumentationRequestMapper.class);

    @PostMapping
	public ResponseEntity<?> demandDocumentation(HttpServletResponse response,
			@RequestBody DocumentationRequestDto documentationRequest) {
        response.setContentType("application/pdf");
        final DocumentationRequest request = requestMapper.map(documentationRequest);
        documentationPort.documentationOnDemand(response, request);
        return ResponseEntity.ok().build();
    }
}
