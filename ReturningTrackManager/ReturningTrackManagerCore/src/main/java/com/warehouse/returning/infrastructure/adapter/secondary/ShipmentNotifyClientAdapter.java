package com.warehouse.returning.infrastructure.adapter.secondary;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.returning.domain.enumeration.ErrorCode;
import com.warehouse.returning.domain.enumeration.ResponseStatus;
import com.warehouse.returning.domain.helper.Result;
import com.warehouse.returning.domain.port.secondary.ShipmentNotifyClientPort;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ShipmentReturnRequestApi;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.RequestMapper;
import com.warehouse.tools.shipment.ShipmentProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentNotifyClientAdapter implements ShipmentNotifyClientPort {

    private final ShipmentProperties properties;

    private final RestClient restClient;

    public ShipmentNotifyClientAdapter(final ShipmentProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder().baseUrl(properties.getUrl()).build();
    }

    @Override
    public Result<ResponseStatus, ErrorCode> notifyReturnChanged(final ReturnPackageSnapshot snapshot) {
        final ShipmentReturnRequestApi request = RequestMapper.toShipmentReturnRequestApi(snapshot);
        final ResponseEntity<Void> responseEntity = restClient.put()
                .uri("/v2/api/shipments/returns")
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity();

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return Result.success(ResponseStatus.OK);
        } else {
            return Result.failure(ErrorCode.of(responseEntity.getStatusCode()));
        }
    }
}
