package com.warehouse.returning.infrastructure.adapter.primary;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.vo.ChangeReasonCodeRequest;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.ChangeReasonCodeRequestApi;
import com.warehouse.returning.infrastructure.adapter.primary.api.DeleteReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.ResponseStatus;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnRequestApi;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.RequestMapper;
import com.warehouse.returning.infrastructure.adapter.primary.mapper.ResponseMapper;

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

    @PutMapping("/reason-code")
    public ResponseEntity<?> updateReasonCode(@RequestBody final ChangeReasonCodeRequestApi changeReasonCodeRequest) {
        final ChangeReasonCodeRequest request = RequestMapper.map(changeReasonCodeRequest);
        this.returnPort.changeReasonCode(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{returnPackageId}")
    public ResponseEntity<?> get(@PathVariable final Long returnPackageId) {
        final ReturnPackageId returnId = new ReturnPackageId(returnPackageId);
        final ReturnPackage returnModel = returnPort.getReturn(returnId);
        return new ResponseEntity<>(returnModel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public DeleteReturnResponse delete(@PathVariable Long id) {
        final ReturnPackageId returnPackageId = new ReturnPackageId(id);
        returnPort.delete(returnPackageId);
        return new DeleteReturnResponse(ResponseStatus.OK);
    }


    @ExceptionHandler(RestException.class)
    public ResponseEntity<String> handleRestException(final RestException ex) {
        return ResponseEntity
                .status(ex.getCode())
                .body(ex.getMessage());
    }

}
