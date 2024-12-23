package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.port.secondary.ShipmentRepositoryServicePort;
import com.warehouse.deliveryreturn.domain.vo.Shipment;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception.BusinessException;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception.TechnicalException;
import com.warehouse.tools.shipment.ShipmentProperties;


public class ShipmentRepositoryServiceAdapter implements ShipmentRepositoryServicePort {

    private final RestClient restClient;
    
    private final ShipmentProperties shipmentProperties;

    private final Logger logger = LoggerFactory.getLogger(ShipmentRepositoryServicePort.class);

    public ShipmentRepositoryServiceAdapter(final ShipmentProperties shipmentProperties) {
        this.shipmentProperties = shipmentProperties;
        this.restClient = RestClient.builder().baseUrl(shipmentProperties.getUrl()).build();
    }

    private Shipment emptyShipment() {
        return Shipment.builder().build();
    }

    @Override
    public Shipment downloadShipment(final ShipmentId shipmentId) {
        final ResponseEntity<ShipmentDto> parcelResponse = restClient
                .get()
                .uri("/v2/api/shipments/{value}", shipmentId.getValue())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,
                        (req, res) -> {
                            logger.error("Could not establish connection");
                            throw new TechnicalException(res.getStatusCode().value(), res.getStatusText());
                        })
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> {
                            logger.error("Shipment {} was not found", shipmentId);
                            throw new BusinessException(res.getStatusCode().value(), res.getStatusText());
                        })
                .toEntity(ShipmentDto.class);
        if (parcelResponse.getStatusCode().is2xxSuccessful()) {
            if (parcelResponse.getBody() != null) {
                return Shipment.from(parcelResponse.getBody());
            }
        }
        return emptyShipment();
    }
}
