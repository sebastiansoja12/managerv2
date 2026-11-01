package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.geocoding.api.GeocodingAddressDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesService;
import com.warehouse.logisticsdelivery.application.port.secondary.DestinationDepartmentResolverPort;
import com.warehouse.logisticsdelivery.domain.vo.RecipientAddress;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.UncoveredPostalCodePolicyDto;
import com.warehouse.organisationstructure.deliveryarea.api.DeliveryAreaApiService;
import com.warehouse.organisationstructure.deliveryarea.api.dto.DeliveryLocationDto;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.voronoi.VoronoiRequestDto;
import com.warehouse.voronoi.VoronoiResponseDto;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.dto.AddressDto;
import com.warehouse.voronoi.dto.CoordinatesDto;
import com.warehouse.voronoi.dto.DepartmentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class DestinationDepartmentResolverAdapter implements DestinationDepartmentResolverPort {

    private static final Logger log = LoggerFactory.getLogger(DestinationDepartmentResolverAdapter.class);

    private static final String ACTIVE_STATUS = "ACTIVE";

    private final GeocodingCoordinatesService geocodingCoordinatesService;

    private final DeliveryAreaApiService deliveryAreaApiService;

    private final VoronoiService voronoiService;

    private final DepartmentApiService departmentApiService;

    private final OperatorConfigurationApiService operatorConfigurationApiService;

    public DestinationDepartmentResolverAdapter(final GeocodingCoordinatesService geocodingCoordinatesService,
                                                final DeliveryAreaApiService deliveryAreaApiService,
                                                final VoronoiService voronoiService,
                                                final DepartmentApiService departmentApiService,
                                                final OperatorConfigurationApiService operatorConfigurationApiService) {
        this.geocodingCoordinatesService = geocodingCoordinatesService;
        this.deliveryAreaApiService = deliveryAreaApiService;
        this.voronoiService = voronoiService;
        this.departmentApiService = departmentApiService;
        this.operatorConfigurationApiService = operatorConfigurationApiService;
    }

    @Override
    public Optional<DepartmentId> resolve(final RecipientAddress recipientAddress) {
        try {
            final GeocodingCoordinatesDto coordinates = this.geocodingCoordinatesService.findCoordinates(
                    new GeocodingAddressDto(recipientAddress.city(), recipientAddress.street(),
                            recipientAddress.postalCode(), recipientAddress.countryCode()));
            if (coordinates == null || coordinates.latitude() == null || coordinates.longitude() == null) {
                return Optional.empty();
            }
            return this.deliveryAreaApiService.findServingDepartment(
                            new DeliveryLocationDto(coordinates.latitude(), coordinates.longitude()))
                    .or(() -> uncoveredAddressDepartment(recipientAddress, coordinates));
        } catch (final RuntimeException exception) {
            log.error("Destination department lookup failed for {}, {}: {}", recipientAddress.postalCode(),
                    recipientAddress.city(), exception.getMessage());
            return Optional.empty();
        }
    }

    private Optional<DepartmentId> uncoveredAddressDepartment(final RecipientAddress recipientAddress,
                                                              final GeocodingCoordinatesDto coordinates) {
        final Optional<DepartmentId> nearestDepartment = nearestDepartment(recipientAddress, coordinates);
        final UncoveredPostalCodePolicyDto policy = uncoveredPostalCodePolicy(nearestDepartment.orElse(null));
        if (policy == UncoveredPostalCodePolicyDto.BLOCK) {
            log.warn("Address {}, {} is not covered by any delivery area and automatic assignment is blocked",
                    recipientAddress.postalCode(), recipientAddress.city());
            return Optional.empty();
        }
        if (policy == UncoveredPostalCodePolicyDto.WARN) {
            log.warn("Address {}, {} is not covered by any delivery area, the nearest department is assigned",
                    recipientAddress.postalCode(), recipientAddress.city());
        }
        return nearestDepartment;
    }

    private UncoveredPostalCodePolicyDto uncoveredPostalCodePolicy(final DepartmentId nearestDepartmentId) {
        final DepartmentConfigurationDto configuration = nearestDepartmentId == null
                ? this.operatorConfigurationApiService.getCurrentDepartmentConfiguration()
                : this.operatorConfigurationApiService.getDepartmentConfiguration(nearestDepartmentId);
        if (configuration == null || configuration.deliveryAreaPolicy() == null
                || configuration.deliveryAreaPolicy().uncoveredPostalCodePolicy() == null) {
            return UncoveredPostalCodePolicyDto.WARN;
        }
        return configuration.deliveryAreaPolicy().uncoveredPostalCodePolicy();
    }

    private Optional<DepartmentId> nearestDepartment(final RecipientAddress recipientAddress,
                                                     final GeocodingCoordinatesDto coordinates) {
        final List<DepartmentDto> departments = this.departmentApiService.getAllDepartments()
                .stream()
                .filter(department -> department.status() == null || ACTIVE_STATUS.equals(department.status()))
                .filter(department -> department.coordinates() != null
                        && department.coordinates().latitude() != null
                        && department.coordinates().longitude() != null)
                .map(department -> new DepartmentDto(department.departmentCode(), department.city(),
                        department.street(), department.zipCode(), department.countryCode(),
                        new CoordinatesDto(department.coordinates().latitude(), department.coordinates().longitude())))
                .toList();
        if (departments.isEmpty()) {
            return Optional.empty();
        }
        final VoronoiResponseDto response = this.voronoiService.findFastestRoute(new VoronoiRequestDto(
                new AddressDto(recipientAddress.city(), recipientAddress.street(), recipientAddress.postalCode(),
                        countryCode(recipientAddress.countryCode())),
                new CoordinatesDto(coordinates.latitude(), coordinates.longitude()),
                departments));
        if (response == null || response.departmentCode() == null || response.departmentCode().value() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.departmentApiService.getDepartmentByCode(
                        new DepartmentCode(response.departmentCode().value())))
                .map(department -> department.departmentId() == null ? null : new DepartmentId(department.departmentId()));
    }

    private static CountryCode countryCode(final String countryCode) {
        return countryCode == null ? null : CountryCode.valueOf(countryCode);
    }
}
