package com.warehouse.deliverytoken.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.support.RestGatewaySupport;

import com.warehouse.deliverytoken.domain.model.Parcel;
import com.warehouse.deliverytoken.domain.model.ParcelId;
import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.api.dto.ParcelDto;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.CommunicationException;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.mapper.ParcelResponseMapper;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.property.ShipmentProperty;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ParcelServiceAdapter extends RestGatewaySupport implements ParcelServicePort {

    private final ShipmentProperty shipmentProperty;

    private final ParcelResponseMapper responseMapper = getMapper(ParcelResponseMapper.class);

    @Override
    public Parcel downloadParcel(ParcelId parcelId) {
        final ShipmentConfiguration shipmentConfiguration = new ShipmentConfiguration(shipmentProperty);
        try {
            return downloadParcel(parcelId, shipmentConfiguration);
        } catch (ResourceAccessException exception) {
            log.info("Connection with {} could not be established", shipmentConfiguration.getName());
            throw new CommunicationException(shipmentConfiguration.getName());
        }
    }

	private Parcel downloadParcel(ParcelId parcelId, ShipmentConfiguration shipmentConfiguration)
            throws ResourceAccessException {

		final ResponseEntity<ParcelDto> responseEntity = getRestTemplate()
				.getForEntity(String.format(shipmentConfiguration.getUrl(), parcelId.value()), ParcelDto.class);

		return responseMapper.map(Objects.requireNonNull(responseEntity.getBody()));
	}

    @Value
    private static class ShipmentConfiguration {

        ShipmentProperty shipmentProperty;

        public String getUrl() {
            return shipmentProperty.getUrl();
        }

        public String getName() {
            return shipmentProperty.getName();
        }
    }
}
