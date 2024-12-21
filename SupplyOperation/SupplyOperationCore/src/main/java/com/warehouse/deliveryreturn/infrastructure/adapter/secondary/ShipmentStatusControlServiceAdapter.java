package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestGatewaySupport;

import com.warehouse.deliveryreturn.domain.port.secondary.ShipmentStatusControlServicePort;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusShipmentRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.UpdateStatusShipmentRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.UpdateStatusParcelRequestMapper;
import com.warehouse.tools.shipment.ShipmentProperties;


public class ShipmentStatusControlServiceAdapter extends RestGatewaySupport
		implements ShipmentStatusControlServicePort {

    private final RestClient restClient;

    private final ShipmentProperties shipmentProperties;

    private final UpdateStatusParcelRequestMapper updateStatusParcelRequestMapper =
            getMapper(UpdateStatusParcelRequestMapper.class);

    public ShipmentStatusControlServiceAdapter(final ShipmentProperties shipmentProperties) {
        this.restClient = RestClient.builder().baseUrl(shipmentProperties.getUrl()).build();
        this.shipmentProperties = shipmentProperties;
    }

    @Override
    public UpdateStatus updateStatus(final UpdateStatusShipmentRequest updateStatusShipmentRequest) {
        final UpdateStatusShipmentRequestDto request = updateStatusParcelRequestMapper.map(updateStatusShipmentRequest);
        return restClient
                .put()
                .uri("/v2/api/shipments/status")
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange((req, res) -> {
                    final HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(res.getStatusCode().value());
                    if (httpStatusCode.is2xxSuccessful()) {
                        return UpdateStatus.OK;
                    }
                    if (httpStatusCode.is5xxServerError()) {
                        return UpdateStatus.ERROR;
                    } else {
                        return UpdateStatus.NOT_OK;
                    }
                });
    }
}
