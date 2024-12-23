package com.warehouse.returntoken.infrastructure.adapter.primary;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.returntoken.domain.model.ReturnTokenResponse;
import com.warehouse.returntoken.domain.port.primary.ReturnTokenPort;
import com.warehouse.returntoken.domain.vo.ReturnTokenRequest;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.ReturnTokenRequestDto;
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
}
