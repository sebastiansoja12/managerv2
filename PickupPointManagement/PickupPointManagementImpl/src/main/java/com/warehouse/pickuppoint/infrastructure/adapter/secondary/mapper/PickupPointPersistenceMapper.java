package com.warehouse.pickuppoint.infrastructure.adapter.secondary.mapper;

import com.warehouse.pickuppoint.domain.model.PickupPoint;
import com.warehouse.pickuppoint.domain.vo.ExternalPointReference;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.OpeningDay;
import com.warehouse.pickuppoint.domain.vo.OpeningInterval;
import com.warehouse.pickuppoint.domain.vo.OpeningSchedule;
import com.warehouse.pickuppoint.domain.vo.OpeningScheduleException;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;
import com.warehouse.pickuppoint.domain.vo.PickupPointCode;
import com.warehouse.pickuppoint.domain.vo.PickupPointContact;
import com.warehouse.pickuppoint.domain.vo.PickupPointServicePolicy;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.OpeningIntervalEmbeddable;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.PickupPointEntity;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.PickupPointOpeningDayEntity;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.PickupPointReadEntity;
import com.warehouse.pickuppoint.infrastructure.adapter.secondary.entity.PickupPointScheduleExceptionEntity;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PickupPointPersistenceMapper {

    public PickupPoint toModel(final PickupPointEntity entity) {
        return PickupPoint.restore(new PickupPointSnapshot(
                entity.getPickupPointId(),
                new PickupPointCode(entity.getCode()),
                entity.getName(),
                entity.getType(),
                entity.getStatus(),
                entity.getCapabilities(),
                address(entity),
                coordinates(entity),
                entity.getDepartmentId(),
                contact(entity),
                entity.getAccessInstructions(),
                openingSchedule(entity),
                servicePolicy(entity),
                externalReference(entity),
                entity.getStatusReason(),
                entity.getVersion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()));
    }

    public PickupPointEntity toEntity(final PickupPoint pickupPoint) {
        return toEntity(pickupPoint, null);
    }

    public PickupPointEntity toEntityForUpdate(final PickupPoint pickupPoint) {
        return toEntity(pickupPoint, pickupPoint.snapshot().version());
    }

    public PickupPointReadEntity toReadEntity(final PickupPointSnapshot snapshot) {
        final PickupPointAddress address = snapshot.address();
        final GeoCoordinates coordinates = snapshot.coordinates();
        final PickupPointServicePolicy servicePolicy = snapshot.servicePolicy();
        return new PickupPointReadEntity(
                snapshot.pickupPointId(),
                snapshot.code().value(),
                snapshot.name(),
                snapshot.type(),
                snapshot.status(),
                joined(snapshot.capabilities()),
                address.countryCode(),
                address.postalCode(),
                address.city(),
                address.street(),
                address.buildingNumber(),
                address.unitNumber(),
                coordinates.latitude(),
                coordinates.longitude(),
                snapshot.departmentId(),
                joined(servicePolicy.allowedShipmentSizes()),
                servicePolicy.acceptsDangerousGoods(),
                snapshot.externalReference() == null ? null : snapshot.externalReference().networkCode(),
                snapshot.version());
    }

    private PickupPointEntity toEntity(final PickupPoint pickupPoint, final Long version) {
        final PickupPointSnapshot snapshot = pickupPoint.snapshot();
        final PickupPointAddress address = snapshot.address();
        final GeoCoordinates coordinates = snapshot.coordinates();
        final PickupPointContact contact = snapshot.contact();
        final OpeningSchedule schedule = snapshot.openingSchedule();
        final PickupPointServicePolicy servicePolicy = snapshot.servicePolicy();
        final ExternalPointReference externalReference = snapshot.externalReference();
        final String pickupPointKey = snapshot.pickupPointId().value().toString();
        final Set<PickupPointOpeningDayEntity> openingDays = schedule.days().stream()
                .map(day -> toEntity(pickupPointKey, day))
                .collect(Collectors.toSet());
        final Set<PickupPointScheduleExceptionEntity> exceptions = schedule.exceptions().stream()
                .map(exception -> toEntity(pickupPointKey, exception))
                .collect(Collectors.toSet());
        return new PickupPointEntity(
                snapshot.pickupPointId(),
                snapshot.code().value(),
                snapshot.name(),
                snapshot.type(),
                snapshot.status(),
                snapshot.capabilities(),
                address.countryCode(),
                address.postalCode(),
                address.city(),
                address.street(),
                address.buildingNumber(),
                address.unitNumber(),
                coordinates.latitude(),
                coordinates.longitude(),
                snapshot.departmentId(),
                contact == null ? null : contact.telephoneNumber(),
                contact == null ? null : contact.email(),
                snapshot.accessInstructions(),
                schedule.timeZone().getId(),
                schedule.mode(),
                openingDays,
                exceptions,
                servicePolicy.allowedShipmentSizes(),
                servicePolicy.acceptsDangerousGoods(),
                externalReference == null ? null : externalReference.networkCode(),
                externalReference == null ? null : externalReference.pointCode(),
                snapshot.statusReason(),
                version,
                snapshot.createdAt(),
                snapshot.updatedAt());
    }

    private PickupPointAddress address(final PickupPointEntity entity) {
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

    private GeoCoordinates coordinates(final PickupPointEntity entity) {
        if (entity.getLatitude() == null || entity.getLongitude() == null) {
            return null;
        }
        return new GeoCoordinates(entity.getLatitude(), entity.getLongitude());
    }

    private PickupPointContact contact(final PickupPointEntity entity) {
        if (entity.getTelephoneNumber() == null && entity.getEmail() == null) {
            return null;
        }
        return new PickupPointContact(entity.getTelephoneNumber(), entity.getEmail());
    }

    private OpeningSchedule openingSchedule(final PickupPointEntity entity) {
        if (entity.getOpeningScheduleMode() == null || entity.getOpeningTimeZone() == null) {
            return null;
        }
        final List<OpeningDay> openingDays = entity.getOpeningDays().stream()
                .map(day -> new OpeningDay(
                        day.getDayOfWeek(),
                        day.getAvailability(),
                        intervals(day.getIntervals())))
                .toList();
        final List<OpeningScheduleException> exceptions = entity.getScheduleExceptions().stream()
                .map(exception -> new OpeningScheduleException(
                        exception.getDate(),
                        exception.getAvailability(),
                        intervals(exception.getIntervals())))
                .toList();
        return new OpeningSchedule(
                ZoneId.of(entity.getOpeningTimeZone()),
                entity.getOpeningScheduleMode(),
                openingDays,
                exceptions);
    }

    private PickupPointServicePolicy servicePolicy(final PickupPointEntity entity) {
        if (entity.getAcceptsDangerousGoods() == null) {
            return null;
        }
        return new PickupPointServicePolicy(
                entity.getAllowedShipmentSizes(),
                entity.getAcceptsDangerousGoods());
    }

    private ExternalPointReference externalReference(final PickupPointEntity entity) {
        if (entity.getExternalNetworkCode() == null || entity.getExternalPointCode() == null) {
            return null;
        }
        return new ExternalPointReference(entity.getExternalNetworkCode(), entity.getExternalPointCode());
    }

    private PickupPointOpeningDayEntity toEntity(final String pickupPointKey, final OpeningDay day) {
        return new PickupPointOpeningDayEntity(
                deterministicId(pickupPointKey + ":day:" + day.dayOfWeek()),
                day.dayOfWeek(),
                day.availability(),
                intervalEntities(day.intervals()));
    }

    private PickupPointScheduleExceptionEntity toEntity(
            final String pickupPointKey,
            final OpeningScheduleException exception) {
        return new PickupPointScheduleExceptionEntity(
                deterministicId(pickupPointKey + ":exception:" + exception.date()),
                exception.date(),
                exception.availability(),
                intervalEntities(exception.intervals()));
    }

    private List<OpeningIntervalEmbeddable> intervalEntities(final List<OpeningInterval> intervals) {
        return intervals.stream()
                .map(interval -> new OpeningIntervalEmbeddable(interval.startMinute(), interval.endMinute()))
                .toList();
    }

    private List<OpeningInterval> intervals(final List<OpeningIntervalEmbeddable> intervals) {
        return intervals.stream()
                .map(interval -> new OpeningInterval(interval.getStartMinute(), interval.getEndMinute()))
                .toList();
    }

    private UUID deterministicId(final String value) {
        return UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    private String joined(final Set<? extends Enum<?>> values) {
        return values.stream()
                .map(Enum::name)
                .sorted()
                .collect(Collectors.joining(","));
    }
}
