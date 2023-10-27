package com.warehouse.parcelstatuschange.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.parcelstatuschange.domain.model.StatusRequest;
import com.warehouse.parcelstatuschange.domain.port.primary.ParcelStatusPort;
import com.warehouse.parcelstatuschange.infrastructure.adapter.primary.api.dto.StatusRequestDto;
import com.warehouse.parcelstatuschange.infrastructure.adapter.primary.mapper.ParcelStatusRequestMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parcel-updates")
@RequiredArgsConstructor
public class ParcelStatusController {

    private final ParcelStatusPort parcelStatusPort;

    private final ParcelStatusRequestMapper requestMapper = getMapper(ParcelStatusRequestMapper.class);

    @PostMapping
    public ResponseEntity<?> updateStatus(@RequestBody StatusRequestDto statusRequest) {
        final StatusRequest request = requestMapper.map(statusRequest);
        parcelStatusPort.updateStatus(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
