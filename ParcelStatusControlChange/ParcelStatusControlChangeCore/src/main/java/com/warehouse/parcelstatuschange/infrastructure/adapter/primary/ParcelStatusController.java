package com.warehouse.parcelstatuschange.infrastructure.adapter.primary;


import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.parcelstatuschange.domain.vo.Status;
import com.warehouse.parcelstatuschange.infrastructure.adapter.primary.mapper.ParcelStatusResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.parcelstatuschange.domain.vo.StatusRequest;
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

    private final ParcelStatusResponseMapper responseMapper = getMapper(ParcelStatusResponseMapper.class);

    @PostMapping
    public ResponseEntity<?> updateStatus(@RequestBody StatusRequestDto statusRequest) {
        final StatusRequest request = requestMapper.map(statusRequest);
        parcelStatusPort.updateStatus(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{parcelId}")
    public ResponseEntity<?> getStatus(@PathVariable Long parcelId) {
        final Status status = parcelStatusPort.getStatus(parcelId);
        return new ResponseEntity<>(responseMapper.map(status), HttpStatus.OK);
    }

}
