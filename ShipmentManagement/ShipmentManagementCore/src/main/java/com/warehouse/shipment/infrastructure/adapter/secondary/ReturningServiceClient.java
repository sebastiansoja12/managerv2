package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;

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
public class ReturningServiceClient implements ReturningServicePort {

    private final ExternalFeignClient externalFeignClient;

    private final ReturnProperties returnProperties;

    public ReturningServiceClient(final ExternalFeignClient externalFeignClient,
                                  final ReturnProperties returnProperties) {
        this.externalFeignClient = externalFeignClient;
        this.returnProperties = returnProperties;
    }

    @Override
    public void notifyShipmentReturn(final ShipmentSnapshot snapshot) {
        log.info("Sending request to returning manager for shipment {}", snapshot.shipmentId().toString());
        final ReturnRequestApi request = OutputRequestMapper.map(snapshot);
        this.externalFeignClient.processReturn(returnUri(), request);
    }

    @Override
    public Map<ShipmentId, ReturnId> shipmentReturnCommand(final ShipmentReturnedCommand shipmentReturnedCommand) {
        log.info("Sending request to returning manager for shipment {}", shipmentReturnedCommand.shipmentId().toString());
        final ReturnRequestApi request = OutputRequestMapper.map(shipmentReturnedCommand);
        final ResponseEntity<ReturnResponseApi> response = this.externalFeignClient.processReturn(returnUri(), request);

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
        this.externalFeignClient.processReturn(returnUri(), request);
    }

    @Override
    public void notifyShipmentReturnCompleted(final ShipmentSnapshot snapshot) {
        log.info("Finishing shipment return in return manager {}", snapshot.shipmentId().toString());
        final ChangeReturnStatusApiRequest request = new ChangeReturnStatusApiRequest(
                new ReturnIdDto(snapshot.returnExternalId().value()), "COMPLETED"
        );
        this.externalFeignClient.completeReturn(completeReturnUri(), request);
    }

    private URI returnUri() {
        return URI.create(returnProperties.getUrl() + returnProperties.getEndpoint());
    }

    private URI completeReturnUri() {
        return URI.create(returnProperties.getUrl() + returnProperties.getEndpoint() + "/complete");
    }
}
