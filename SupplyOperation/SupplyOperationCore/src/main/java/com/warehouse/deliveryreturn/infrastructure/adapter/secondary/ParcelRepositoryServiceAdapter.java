package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.warehouse.deliveryreturn.domain.port.secondary.ParcelRepositoryServicePort;
import com.warehouse.deliveryreturn.domain.vo.Shipment;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto.ShipmentDto;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception.BusinessException;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.exception.TechnicalException;
import com.warehouse.tools.shipment.ShipmentProperties;


public class ParcelRepositoryServiceAdapter implements ParcelRepositoryServicePort {

    private final RestClient restClient;
    
    private final ShipmentProperties shipmentProperties;

    private final Logger logger = LoggerFactory.getLogger(ParcelRepositoryServicePort.class);

    public ParcelRepositoryServiceAdapter(ShipmentProperties shipmentProperties) {
        this.shipmentProperties = shipmentProperties;
        this.restClient = RestClient.builder().baseUrl(shipmentProperties.getUrl()).build();
    }

    @Override
    public Shipment downloadParcel(Long parcelId) {
        final ResponseEntity<ShipmentDto> parcelResponse = restClient
                .get()
                .uri("/v2/api/shipments/{parcelId}", parcelId)
                .retrieve()
				.onStatus(HttpStatusCode::is5xxServerError,
						(req, res) -> {
							logger.error("Could not establish connection");
                            throw new TechnicalException(res.getStatusCode().value(), res.getStatusText());
						})
                .onStatus(HttpStatusCode::is4xxClientError,
                        (req, res) -> {
                            logger.error("Parcel {} was not found", parcelId);
                            throw new BusinessException(res.getStatusCode().value(), res.getStatusText());
                        })
                .toEntity(ShipmentDto.class);
        if (parcelResponse.getStatusCode().is2xxSuccessful()) {
            if (parcelResponse.getBody() != null) {
                return Shipment.from(parcelResponse.getBody());
            }
        }
        return emptyResponseParcel();
    }

    private Shipment emptyResponseParcel() {
        return Shipment.builder().build();
    }
}
