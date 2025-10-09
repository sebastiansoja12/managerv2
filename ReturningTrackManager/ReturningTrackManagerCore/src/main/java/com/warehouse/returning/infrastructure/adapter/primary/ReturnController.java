package com.warehouse.returning.infrastructure.adapter.primary;

import com.warehouse.returning.infrastructure.adapter.primary.mapper.ResponseMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;
import com.warehouse.returning.domain.vo.ReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.DeleteReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.ResponseStatus;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnRequestApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/returns")
@RequiredArgsConstructor
@Slf4j
public class ReturnController {

    private final ReturnPort returnPort;

    @PostMapping
    public ResponseEntity<?> process(
            @RequestHeader("X-API-KEY") final String apiKey,
            @RequestBody final ReturnRequestApi returnApiRequest) {

        // TODO
        if (StringUtils.isEmpty(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
		log.info("Detected request for returning from user: {} from department: {}",
				returnApiRequest.userId().value(), returnApiRequest.departmentCode().value());

        final ReturnRequest request = ReturnRequest.from(returnApiRequest);
        
        
        final ReturnResponse response = this.returnPort.process(request);
        
        return ResponseEntity.ok().body(ResponseMapper.toResponseApi(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        final ReturnId returnId = new ReturnId(id);
        final ReturnModel returnModel = returnPort.getReturn(returnId);
        return new ResponseEntity<>(returnModel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public DeleteReturnResponse delete(@PathVariable Long id) {
        final ReturnId returnId = new ReturnId(id);
        returnPort.delete(returnId);
        return new DeleteReturnResponse(ResponseStatus.OK);
    }
}
