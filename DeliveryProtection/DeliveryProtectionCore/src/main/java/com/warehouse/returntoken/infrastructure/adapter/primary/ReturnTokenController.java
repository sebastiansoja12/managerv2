package com.warehouse.returntoken.infrastructure.adapter.primary;

import com.warehouse.returntoken.domain.model.Parcel;
import com.warehouse.returntoken.domain.port.primary.ReturnTokenPort;
import com.warehouse.returntoken.domain.vo.Token;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.ParcelDto;
import com.warehouse.returntoken.infrastructure.adapter.primary.mapper.ReturnTokenRequestMapper;
import com.warehouse.returntoken.infrastructure.adapter.primary.mapper.ReturnTokenResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.mapstruct.factory.Mappers.getMapper;

@RestController
@RequestMapping("/return-tokens")
@RequiredArgsConstructor
public class ReturnTokenController {

    private final ReturnTokenPort returnTokenPort;

    private final ReturnTokenRequestMapper requestMapper = getMapper(ReturnTokenRequestMapper.class);
    private final ReturnTokenResponseMapper responseMapper = getMapper(ReturnTokenResponseMapper.class);

    @PostMapping
    public ResponseEntity<?> processReturnToken(@RequestBody ParcelDto parcelRequest) {
        final Parcel parcel = requestMapper.map(parcelRequest);
        final Token token = returnTokenPort.determine(parcel);
        return ResponseEntity.ok(responseMapper.map(token));
    }
}
