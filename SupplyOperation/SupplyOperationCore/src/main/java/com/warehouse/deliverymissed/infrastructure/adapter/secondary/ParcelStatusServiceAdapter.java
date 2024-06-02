package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.deliverymissed.infrastructure.adapter.secondary.api.dto.UpdateStatusParcelRequestDto;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper.UpdateStatusParcelRequestMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.warehouse.deliverymissed.domain.port.secondary.ParcelStatusServicePort;
import com.warehouse.deliverymissed.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.tools.parcelstatus.ParcelStatusProperties;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ParcelStatusServiceAdapter implements ParcelStatusServicePort {

    private final RestClient restClient;

    private final ParcelStatusProperties parcelStatusProperties;

    private final UpdateStatusParcelRequestMapper updateStatusParcelRequestMapper =
            getMapper(UpdateStatusParcelRequestMapper.class);

    public ParcelStatusServiceAdapter(ParcelStatusProperties parcelStatusProperties) {
        this.parcelStatusProperties = parcelStatusProperties;
        this.restClient = RestClient.builder().baseUrl(parcelStatusProperties.getUrl()).build();
    }

    @Override
    public void updateParcelStatus(UpdateStatusParcelRequest updateStatusParcelRequest) {
        final UpdateStatusParcelRequestDto request = updateStatusParcelRequestMapper.map(updateStatusParcelRequest);
        restClient
                .post()
                .uri("/v2/api/{url}", parcelStatusProperties.getEndpoint())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        (req, res) -> log.warn("Successfully updated parcel status"))
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> log.warn("Error while updating parcels status"))
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> log.warn("Critical error while updating parcels status"));
    }
}
