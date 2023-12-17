package com.warehouse.returning.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;


import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.infrastructure.adapter.primary.api.ResponseStatus;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.ReturnResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;
import com.warehouse.returning.domain.vo.ReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.DeleteReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturningRequestDto;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.ReturnRequestMapper;


@RestController
@RequestMapping("/returns")
@RequiredArgsConstructor
@Slf4j
public class ReturnController {

    private final ReturnPort returnPort;

    private final ReturnRequestMapper requestMapper = getMapper(ReturnRequestMapper.class);

    private final ReturnResponseMapper responseMapper = getMapper(ReturnResponseMapper.class);


    @PostMapping
    public ResponseEntity<?> process(@RequestBody ReturningRequestDto returningRequest) {
        
		log.info("Detected request for returning from user: {} from depot: {}",
				returningRequest.getUsername().getValue(), returningRequest.getDepotCode().getValue());

        final ReturnRequest request = requestMapper.map(returningRequest);
        
        
        final ReturnResponse response = returnPort.process(request);
        
        
        return new ResponseEntity<>(responseMapper.map(response), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        final ReturnId returnId = new ReturnId(id);
        final ReturnModel returnModel = returnPort.getReturn(returnId);
        return new ResponseEntity<>(responseMapper.map(returnModel), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public DeleteReturnResponse delete(@PathVariable Long id) {
        final ReturnId returnId = new ReturnId(id);
        returnPort.delete(returnId);
        return new DeleteReturnResponse(ResponseStatus.OK);
    }
}
