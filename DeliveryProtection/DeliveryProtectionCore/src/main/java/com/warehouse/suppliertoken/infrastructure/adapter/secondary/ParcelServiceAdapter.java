package com.warehouse.suppliertoken.infrastructure.adapter.secondary;

import com.warehouse.suppliertoken.domain.model.Parcel;
import com.warehouse.suppliertoken.domain.model.ParcelId;
import com.warehouse.suppliertoken.domain.port.secondary.ParcelServicePort;
import com.warehouse.suppliertoken.infrastructure.adapter.secondary.api.dto.ParcelDto;
import com.warehouse.suppliertoken.infrastructure.adapter.secondary.mapper.ParcelResponseMapper;
import com.warehouse.suppliertoken.infrastructure.adapter.secondary.property.ShipmentProperty;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.support.RestGatewaySupport;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class ParcelServiceAdapter extends RestGatewaySupport implements ParcelServicePort {

    private final ShipmentProperty shipmentProperty;

    private final ParcelResponseMapper responseMapper = getMapper(ParcelResponseMapper.class);

    @Override
    public List<Parcel> downloadParcels(List<ParcelId> parcelIds) {
        final ShipmentConfiguration shipmentConfiguration = new ShipmentConfiguration(shipmentProperty);
        return downloadParcels(parcelIds, shipmentConfiguration);
    }

    private List<Parcel> downloadParcels(List<ParcelId> parcelIds, ShipmentConfiguration shipmentConfiguration) {
        return parcelIds
                .stream()
                .map(parcelId -> {
                    final ResponseEntity<ParcelDto> responseEntity = getRestTemplate()
                            .getForEntity(String.format(shipmentConfiguration.getUrl(), parcelId.value()),
                                    ParcelDto.class);
                    return responseMapper.map(Objects.requireNonNull(responseEntity.getBody()));
                }).collect(Collectors.toList());
    }

    @Value
    private static class ShipmentConfiguration {

        ShipmentProperty shipmentProperty;

        public String getUrl() {
            return shipmentProperty.getUrl();
        }
    }
}
