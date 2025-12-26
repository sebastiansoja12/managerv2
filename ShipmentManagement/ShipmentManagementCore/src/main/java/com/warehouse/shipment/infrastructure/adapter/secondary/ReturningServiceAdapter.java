package com.warehouse.shipment.infrastructure.adapter.secondary;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestClient;

import com.warehouse.shipment.domain.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnRequestApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.OutputRequestMapper;
import com.warehouse.tools.returning.ReturnProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReturningServiceAdapter implements ReturningServicePort {

    private final RestClient restClient;

    private final ReturnProperties returnProperties;

    public ReturningServiceAdapter(final ReturnProperties returnProperties) {
        this.returnProperties = returnProperties;
        this.restClient = RestClient.builder().baseUrl("http://localhost:8070").build();
    }

    @Override
    public void notifyShipmentReturn(final ShipmentSnapshot snapshot) {
        log.info("Sending request to returning manager for shipment {}", snapshot.shipmentId().toString());
        final ReturnRequestApi request = OutputRequestMapper.map(snapshot);
        restClient
                .post()
                .uri("/v2/api/returns")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(
                        SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
                ))
                .body(request)
                .retrieve()
                .toEntity(Void.class);
    }

    @Override
    public void notifyShipmentUpdated(final ShipmentSnapshot snapshot) {
        log.info("Updating shipment in return manager {}", snapshot.shipmentId().toString());
        final ReturnRequestApi request = OutputRequestMapper.map(snapshot);
        restClient
                .post()
                .uri("/v2/api/returns")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(
                        SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
                ))
                .body(request)
                .retrieve()
                .toEntity(Void.class);
    }
}
