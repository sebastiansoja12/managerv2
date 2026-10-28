package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.geocoding.api.GeocodingAddressDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesService;
import com.warehouse.organisationstructure.deliveryarea.api.DeliveryAreaApiService;
import com.warehouse.organisationstructure.deliveryarea.api.dto.DeliveryLocationDto;
import com.warehouse.shipment.application.port.secondary.DeliveryAreaServicePort;
import com.warehouse.shipment.domain.vo.Address;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class DeliveryAreaServiceAdapter implements DeliveryAreaServicePort {

    private final GeocodingCoordinatesService geocodingCoordinatesService;

    private final DeliveryAreaApiService deliveryAreaApiService;

    public DeliveryAreaServiceAdapter(final GeocodingCoordinatesService geocodingCoordinatesService,
                                      final DeliveryAreaApiService deliveryAreaApiService) {
        this.geocodingCoordinatesService = geocodingCoordinatesService;
        this.deliveryAreaApiService = deliveryAreaApiService;
    }

    @Override
    public Optional<DepartmentId> findDeliveryDepartment(final Address address) {
        try {
            final GeocodingCoordinatesDto coordinates = geocodingCoordinatesService.findCoordinates(
                    new GeocodingAddressDto(address.getCity(), address.getStreet(), address.getPostalCode()));
            if (coordinates == null || coordinates.latitude() == null || coordinates.longitude() == null) {
                return Optional.empty();
            }
            return deliveryAreaApiService.findServingDepartment(
                    new DeliveryLocationDto(coordinates.latitude(), coordinates.longitude()));
        } catch (final RuntimeException exception) {
            log.warn("Delivery area lookup failed for {}, {}: {}", address.getPostalCode(), address.getCity(),
                    exception.getMessage());
            return Optional.empty();
        }
    }
}
