package com.warehouse.returning.infrastructure.adapter.primary;

import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.vo.ReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.ReturnRequestMapper;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.ReturnResponseMapper;
import com.warehouse.returning.infrastructure.api.dto.ReturningRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.mapstruct.factory.Mappers.getMapper;


@RequestMapping("/returns")
@RestController
@RequiredArgsConstructor
public class ReturnController {

    private final ReturnPort returnPort;

    private final ReturnRequestMapper requestMapper = getMapper(ReturnRequestMapper.class);

    private final ReturnResponseMapper responseMapper = getMapper(ReturnResponseMapper.class);


    @PostMapping
    public ResponseEntity<?> processReturning(@RequestBody ReturningRequestDto returningRequest) {
        final ReturnRequest request = requestMapper.map(returningRequest);
        final ReturnResponse response = returnPort.process(request);
        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }
}
