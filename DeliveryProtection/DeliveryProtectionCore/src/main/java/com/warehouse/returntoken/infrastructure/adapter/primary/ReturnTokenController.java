package com.warehouse.returntoken.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.port.primary.ReturnTokenPort;
import com.warehouse.returntoken.domain.vo.ReturnToken;
import com.warehouse.returntoken.domain.vo.ReturnTokenRequest;
import com.warehouse.returntoken.domain.vo.ReturnTokenResponse;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.ReturnTokenDto;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.ReturnTokenRequestDto;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.ReturnTokenVerifyDto;
import com.warehouse.returntoken.infrastructure.adapter.primary.mapper.ReturnTokenRequestMapper;
import com.warehouse.returntoken.infrastructure.adapter.primary.mapper.ReturnTokenResponseMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/return-tokens")
@RequiredArgsConstructor
public class ReturnTokenController {

    private final ReturnTokenPort returnTokenPort;
    private final ReturnTokenRequestMapper requestMapper = getMapper(ReturnTokenRequestMapper.class);
    private final ReturnTokenResponseMapper responseMapper = getMapper(ReturnTokenResponseMapper.class);

    @PostMapping
    public ResponseEntity<?> processReturnToken(@RequestBody final ReturnTokenRequestDto returnTokenRequest) {
        final ReturnTokenRequest request = requestMapper.map(returnTokenRequest);
        final ReturnTokenResponse response = returnTokenPort.signWith(request);
        return ResponseEntity.ok(responseMapper.map(response));
    }

    @GetMapping
    public ResponseEntity<ReturnTokenDto> getReturnToken(@Param("shipment_id") final Long value) {
        final ShipmentId shipmentId = new ShipmentId(value);
        final ReturnToken returnToken = returnTokenPort.getReturnToken(shipmentId);
        return ResponseEntity.ok(responseMapper.map(returnToken));
    }

    @GetMapping("/return_token")
    public ResponseEntity<ReturnTokenVerifyDto> verifyReturnToken(@Param("return_token") final String value) {
        final ReturnToken returnToken = new ReturnToken(value);
        final Boolean valid = returnTokenPort.verify(returnToken);
        return ResponseEntity.ok(new ReturnTokenVerifyDto(valid));
    }
}
