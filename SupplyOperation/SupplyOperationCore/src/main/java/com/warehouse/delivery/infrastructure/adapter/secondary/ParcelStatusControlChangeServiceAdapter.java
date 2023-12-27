package com.warehouse.delivery.infrastructure.adapter.secondary;


import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.delivery.domain.port.secondary.ParcelStatusControlChangeServicePort;
import com.warehouse.delivery.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.delivery.infrastructure.adapter.secondary.api.UpdateStatus;
import com.warehouse.delivery.infrastructure.adapter.secondary.api.UpdateStatusParcelRequestDto;
import com.warehouse.delivery.infrastructure.adapter.secondary.mapper.UpdateStatusParcelRequestMapper;
import com.warehouse.tools.parcelstatus.ParcelStatusProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParcelStatusControlChangeServiceAdapter implements ParcelStatusControlChangeServicePort {

    private final RestClient restClient;

    private final ParcelStatusProperties parcelStatusProperties;

    private final UpdateStatusParcelRequestMapper updateStatusParcelRequestMapper =
            getMapper(UpdateStatusParcelRequestMapper.class);

    public ParcelStatusControlChangeServiceAdapter(ParcelStatusProperties parcelStatusProperties) {
        this.restClient = RestClient.builder().baseUrl(parcelStatusProperties.getUrl()).build();
        this.parcelStatusProperties = parcelStatusProperties;
    }

    @Override
    public UpdateStatus updateParcelStatus(UpdateStatusParcelRequest updateStatusParcelRequest) {
        final UpdateStatusParcelRequestDto request = updateStatusParcelRequestMapper.map(updateStatusParcelRequest);
        final ResponseEntity<Void> updateStatus = restClient.post()
                .uri("/v2/api/{url}", parcelStatusProperties.getEndpoint())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toBodilessEntity();
        if (updateStatus.getStatusCode().is2xxSuccessful()) {
            log.info("Parcel {} was successfully updated", updateStatusParcelRequest.getParcelId());
            return UpdateStatus.OK;
        }
        log.info("Parcel {} was not updated", updateStatusParcelRequest.getParcelId());
        return UpdateStatus.NOT_OK;
    }
}
