package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestGatewaySupport;

import com.warehouse.deliveryreturn.domain.port.secondary.ShipmentStatusControlServicePort;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.UpdateStatusParcelRequestDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper.UpdateStatusParcelRequestMapper;
import com.warehouse.tools.parcelstatus.ParcelStatusProperties;


public class ShipmentStatusControlServiceAdapter extends RestGatewaySupport
		implements ShipmentStatusControlServicePort {

    private final RestClient restClient;

    private final ParcelStatusProperties parcelStatusProperties;

    private final UpdateStatusParcelRequestMapper updateStatusParcelRequestMapper =
            getMapper(UpdateStatusParcelRequestMapper.class);

    public ShipmentStatusControlServiceAdapter(ParcelStatusProperties parcelStatusProperties) {
        this.restClient = RestClient.builder().baseUrl(parcelStatusProperties.getUrl()).build();
        this.parcelStatusProperties = parcelStatusProperties;
    }

    @Override
    public UpdateStatus updateStatus(final UpdateStatusParcelRequest updateStatusParcelRequest) {
        final UpdateStatusParcelRequestDto request = updateStatusParcelRequestMapper.map(updateStatusParcelRequest);
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
