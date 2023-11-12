package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import com.warehouse.deliveryreturn.domain.port.secondary.ParcelStatusControlChangeServicePort;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.UpdateStatusParcelRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.UpdateStatusParcelRequestMapper;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.ParcelStatusProperty;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestGatewaySupport;

import static org.mapstruct.factory.Mappers.getMapper;


public class ParcelStatusControlChangeServiceAdapter extends RestGatewaySupport
		implements ParcelStatusControlChangeServicePort {

    private final RestClient restClient;

    private final ParcelStatusProperty parcelStatusProperty;

    private final UpdateStatusParcelRequestMapper updateStatusParcelRequestMapper =
            getMapper(UpdateStatusParcelRequestMapper.class);

    public ParcelStatusControlChangeServiceAdapter(ParcelStatusProperty parcelStatusProperty) {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8080").build();
        this.parcelStatusProperty = parcelStatusProperty;
    }

    @Override
    public UpdateStatus updateStatus(UpdateStatusParcelRequest updateStatusParcelRequest) {
        final UpdateStatusParcelRequestDto request = updateStatusParcelRequestMapper.map(updateStatusParcelRequest);
        final ResponseEntity<Void> updateStatus = restClient.post()
                .uri("/v2/api/{url}", parcelStatusProperty.getEndpoint())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {})
                .toBodilessEntity();
        if (updateStatus.getStatusCode().is2xxSuccessful()) {
            return UpdateStatus.OK;
        }
        return UpdateStatus.NOT_OK;
    }
}
