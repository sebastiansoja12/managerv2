package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.warehouse.deliverymissed.domain.port.secondary.ShipmentUpdateServicePort;
import com.warehouse.deliverymissed.domain.vo.UpdateStatusShipmentRequest;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.api.dto.UpdateStatusShipmentRequestDto;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.mapper.UpdateStatusParcelRequestMapper;
import com.warehouse.tools.parcelstatus.ShipmentStatusProperties;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ShipmentUpdateServiceAdapter implements ShipmentUpdateServicePort {

    private final RestClient restClient;

    private final ShipmentStatusProperties shipmentStatusProperties;

    private final UpdateStatusParcelRequestMapper updateStatusParcelRequestMapper =
            getMapper(UpdateStatusParcelRequestMapper.class);

    public ShipmentUpdateServiceAdapter(ShipmentStatusProperties shipmentStatusProperties) {
        this.shipmentStatusProperties = shipmentStatusProperties;
        this.restClient = RestClient.builder().baseUrl(shipmentStatusProperties.getUrl()).build();
    }

    @Override
    public void updateShipmentStatus(final UpdateStatusShipmentRequest updateStatusShipmentRequest) {
        final UpdateStatusShipmentRequestDto request = updateStatusParcelRequestMapper.map(updateStatusShipmentRequest);
        restClient
                .post()
                .uri("/v2/api/{url}", shipmentStatusProperties.getEndpoint())
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
