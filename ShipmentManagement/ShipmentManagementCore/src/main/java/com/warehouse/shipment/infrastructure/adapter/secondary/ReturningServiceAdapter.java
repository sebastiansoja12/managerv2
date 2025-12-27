package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestClient;

import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.port.secondary.ReturningServicePort;
import com.warehouse.shipment.domain.vo.ShipmentReturnedCommand;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.*;
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
    public Map<ShipmentId, ReturnId> shipmentReturnCommand(final ShipmentReturnedCommand shipmentReturnedCommand) {
        log.info("Sending request to returning manager for shipment {}", shipmentReturnedCommand.shipmentId().toString());
        final ReturnRequestApi request = OutputRequestMapper.map(shipmentReturnedCommand);
        final ResponseEntity<ReturnResponseApi> response = restClient
                .post()
                .uri("/v2/api/returns")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(
                        SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
                ))
                .body(request)
                .retrieve()
                .toEntity(ReturnResponseApi.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("Error while sending request to returning manager for shipment {}", shipmentReturnedCommand.shipmentId());
            throw new RuntimeException("Error while sending request to returning manager for shipment");
        }

        final Map<ShipmentIdDto, ReturnIdDto> responseMap = response.getBody()
                .processReturn()
                .stream()
                .collect(Collectors.toMap(
                        ProcessReturnDto::shipmentId,
                        ProcessReturnDto::returnId
                ));


        return responseMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> new ShipmentId(entry.getKey().value()),
                        entry -> new ReturnId(entry.getValue().value())
                ));
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

    @Override
    public void notifyShipmentReturnCompleted(final ShipmentSnapshot snapshot) {
        log.info("Finishing shipment return in return manager {}", snapshot.shipmentId().toString());
        final ChangeReturnStatusApiRequest request = new ChangeReturnStatusApiRequest(
                new ReturnIdDto(snapshot.returnExternalId().value()), "COMPLETED"
        );
        restClient
                .put()
                .uri("/v2/api/returns/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(
                        SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
                ))
                .body(request)
                .retrieve()
                .toEntity(Void.class);
    }
}
