package com.warehouse.deliverytoken.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.Objects;

import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.TechnicalException;
import com.warehouse.tools.shipment.ShipmentProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.support.RestGatewaySupport;

import com.warehouse.deliverytoken.domain.vo.Parcel;
import com.warehouse.deliverytoken.domain.vo.ParcelId;
import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.api.dto.ParcelDto;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.CommunicationException;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.mapper.ParcelResponseMapper;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ParcelServiceAdapter extends RestGatewaySupport implements ParcelServicePort {

    private final ShipmentProperties shipmentProperties;

    private final ParcelResponseMapper responseMapper = getMapper(ParcelResponseMapper.class);
    
    private final String url = "/v2/api/%s/%s";

    @Override
    public Parcel downloadParcel(ParcelId parcelId) {
        final ShipmentConfiguration shipmentConfiguration = new ShipmentConfiguration(shipmentProperties);
        try {
            return downloadParcel(parcelId, shipmentConfiguration);
        } catch (HttpClientErrorException exception) {
            log.info("Connection with {} could not be established", shipmentConfiguration.getName());
            throw new CommunicationException(shipmentConfiguration.getName());
        } catch (HttpServerErrorException exception) {
            log.info("Technical exception while connecting with {}", shipmentConfiguration.getName());
            throw new TechnicalException(shipmentConfiguration.getName());
        }
    }

	private Parcel downloadParcel(ParcelId parcelId, ShipmentConfiguration conf)
            throws ResourceAccessException {

		final ResponseEntity<ParcelDto> responseEntity = getRestTemplate().getForEntity(
                String.format(conf.getUrl(), conf.getName()) + parcelId.value(), ParcelDto.class);

		return responseMapper.map(Objects.requireNonNull(responseEntity.getBody()));
	}

    @Value
    private static class ShipmentConfiguration {

        ShipmentProperties shipmentProperties;

        public String getUrl() {
            return shipmentProperties.getUrl();
        }

        public String getName() {
            return shipmentProperties.getName();
        }
    }
}
