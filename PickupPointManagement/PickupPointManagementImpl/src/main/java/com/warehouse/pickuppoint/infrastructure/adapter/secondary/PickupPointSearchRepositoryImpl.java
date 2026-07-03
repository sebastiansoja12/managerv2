package com.warehouse.pickuppoint.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.pickuppoint.application.port.primary.command.SearchPickupPointsCommand;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchItem;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchPage;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchRepository;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.PickupPointReadEntity;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

public class PickupPointSearchRepositoryImpl implements PickupPointSearchRepository {

    private final OperatorFilteredRepository<PickupPointReadEntity> repository;

    public PickupPointSearchRepositoryImpl(final OperatorFilteredRepository<PickupPointReadEntity> repository) {
        this.repository = repository;
    }

    @Override
    public PickupPointSearchPage search(
            final SearchPickupPointsCommand command,
            final Set<DepartmentId> eligibleDepartmentIds) {
        final List<PickupPointSearchItem> matchingPickupPoints = filtered(command, eligibleDepartmentIds).toList();
        final List<PickupPointSearchItem> page = matchingPickupPoints.stream()
                .skip((long) command.page() * command.size())
                .limit(command.size())
                .toList();
        final int totalPages = (matchingPickupPoints.size() + command.size() - 1) / command.size();
        return new PickupPointSearchPage(page, matchingPickupPoints.size(), totalPages);
    }

    private Stream<PickupPointSearchItem> filtered(
            final SearchPickupPointsCommand command,
            final Set<DepartmentId> eligibleDepartmentIds) {
        return this.repository.createCriteria(PickupPointReadEntity.class)
                .asc("code")
                .list()
                .stream()
                .filter(entity -> matches(entity, command, eligibleDepartmentIds))
                .map(this::toSearchItem);
    }

    private boolean matches(
            final PickupPointReadEntity entity,
            final SearchPickupPointsCommand command,
            final Set<DepartmentId> eligibleDepartmentIds) {
        return matchesQuery(entity, command.query())
                && (command.type() == null || entity.getType() == command.type())
                && matchesStatus(entity, command)
                && (command.capability() == null || entity.getCapabilities().contains(command.capability()))
                && (command.departmentId() == null || command.departmentId().equals(entity.getDepartmentId()))
                && (!command.eligibilitySearch() || eligibleDepartmentIds.contains(entity.getDepartmentId()))
                && (command.countryCode() == null || entity.getCountryCode() == command.countryCode())
                && matchesText(entity.getCity(), command.city())
                && matchesText(entity.getExternalNetworkCode(), command.networkCode())
                && matchesBounds(entity, command)
                && (command.shipmentSize() == null
                        || entity.getAllowedShipmentSizes().contains(command.shipmentSize()))
                && (!Boolean.TRUE.equals(command.dangerousGoods()) || entity.acceptsDangerousGoods());
    }

    private boolean matchesStatus(
            final PickupPointReadEntity entity,
            final SearchPickupPointsCommand command) {
        if (command.eligibilitySearch()) {
            return entity.getStatus() == PickupPointStatus.ACTIVE;
        }
        return command.status() == null || entity.getStatus() == command.status();
    }

    private boolean matchesQuery(final PickupPointReadEntity entity, final String query) {
        if (query == null || query.isBlank()) {
            return true;
        }
        final String normalizedQuery = query.trim().toLowerCase(Locale.ROOT);
        final String searchableText = searchableText(entity).toLowerCase(Locale.ROOT);
        return Stream.of(normalizedQuery.split("\\s+"))
                .allMatch(searchableText::contains);
    }

    private String searchableText(final PickupPointReadEntity entity) {
        return Stream.of(
                        entity.getCode(),
                        entity.getName(),
                        entity.getStreet(),
                        entity.getBuildingNumber(),
                        entity.getPostalCode(),
                        entity.getCity())
                .filter(value -> value != null && !value.isBlank())
                .collect(java.util.stream.Collectors.joining(" "));
    }

    private boolean matchesText(final String actualValue, final String expectedValue) {
        return expectedValue == null || expectedValue.isBlank()
                || actualValue != null && actualValue.equalsIgnoreCase(expectedValue.trim());
    }

    private boolean matchesBounds(
            final PickupPointReadEntity entity,
            final SearchPickupPointsCommand command) {
        if (command.west() == null) {
            return true;
        }
        return entity.getLongitude() != null
                && entity.getLatitude() != null
                && entity.getLongitude() >= command.west()
                && entity.getLongitude() <= command.east()
                && entity.getLatitude() >= command.south()
                && entity.getLatitude() <= command.north();
    }

    private PickupPointSearchItem toSearchItem(final PickupPointReadEntity entity) {
        return new PickupPointSearchItem(
                entity.getPickupPointId(),
                entity.getCode(),
                entity.getName(),
                entity.getType(),
                entity.getStatus(),
                entity.getCapabilities(),
                address(entity),
                coordinates(entity),
                entity.getDepartmentId(),
                entity.getAllowedShipmentSizes(),
                entity.acceptsDangerousGoods(),
                entity.getExternalNetworkCode(),
                entity.getVersion());
    }

    private PickupPointAddress address(final PickupPointReadEntity entity) {
        if (entity.getCountryCode() == null) {
            return null;
        }
        return new PickupPointAddress(
                entity.getCountryCode(),
                entity.getPostalCode(),
                entity.getCity(),
                entity.getStreet(),
                entity.getBuildingNumber(),
                entity.getUnitNumber());
    }

    private GeoCoordinates coordinates(final PickupPointReadEntity entity) {
        if (entity.getLatitude() == null || entity.getLongitude() == null) {
            return null;
        }
        return new GeoCoordinates(entity.getLatitude(), entity.getLongitude());
    }

}
