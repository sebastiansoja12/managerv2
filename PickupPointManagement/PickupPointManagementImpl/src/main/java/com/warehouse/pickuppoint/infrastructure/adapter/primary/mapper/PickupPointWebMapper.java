package com.warehouse.pickuppoint.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.api.dto.DepartmentIdDto;
import com.warehouse.pickuppoint.api.dto.PickupPointIdDto;
import com.warehouse.pickuppoint.application.port.primary.command.ChangePickupPointStatusCommand;
import com.warehouse.pickuppoint.application.port.primary.command.CreatePickupPointCommand;
import com.warehouse.pickuppoint.application.port.primary.command.PickupPointConfigurationCommand;
import com.warehouse.pickuppoint.application.port.primary.command.SearchPickupPointsCommand;
import com.warehouse.pickuppoint.application.port.primary.command.UpdatePickupPointCommand;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointPageResult;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointListItemResult;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointResult;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import com.warehouse.pickuppoint.domain.vo.ExternalPointReference;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.OpeningDay;
import com.warehouse.pickuppoint.domain.vo.OpeningInterval;
import com.warehouse.pickuppoint.domain.vo.OpeningSchedule;
import com.warehouse.pickuppoint.domain.vo.OpeningScheduleException;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;
import com.warehouse.pickuppoint.domain.vo.PickupPointCode;
import com.warehouse.pickuppoint.domain.vo.PickupPointContact;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;
import com.warehouse.pickuppoint.domain.vo.PickupPointServicePolicy;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchItem;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.ChangePickupPointStatusApiRequest;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.CreatePickupPointApiRequest;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.ExternalPointReferenceApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.GeoCoordinatesApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.OpeningDayApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.OpeningIntervalApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.OpeningScheduleApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.OpeningScheduleExceptionApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.PickupPointAddressApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.PickupPointApiResponse;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.PickupPointAvailabilityApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.PickupPointConfigurationApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.PickupPointContactApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.PickupPointDepartmentApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.PickupPointPageApiResponse;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.PickupPointServicePolicyApi;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.api.UpdatePickupPointApiRequest;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class PickupPointWebMapper {

    public CreatePickupPointCommand toCommand(final CreatePickupPointApiRequest request) {
        return new CreatePickupPointCommand(
                new PickupPointCode(request.code()),
                configuration(request));
    }

    public UpdatePickupPointCommand toCommand(
            final PickupPointId pickupPointId,
            final UpdatePickupPointApiRequest request) {
        return new UpdatePickupPointCommand(
                pickupPointId,
                configuration(request));
    }

    public ChangePickupPointStatusCommand toCommand(
            final PickupPointId pickupPointId,
            final ChangePickupPointStatusApiRequest request) {
        return new ChangePickupPointStatusCommand(
                pickupPointId,
                request.status(),
                request.reason());
    }

    public SearchPickupPointsCommand toSearchCommand(
            final String query,
            final PickupPointType type,
            final PickupPointStatus status,
            final PickupPointCapability capability,
            final Long departmentId,
            final CountryCode countryCode,
            final String city,
            final String networkCode,
            final double[] bounds,
            final PickupPointShipmentSize shipmentSize,
            final Boolean dangerousGoods,
            final int page,
            final int size) {
        return new SearchPickupPointsCommand(
                query,
                type,
                status,
                capability,
                departmentId == null ? null : new DepartmentId(departmentId),
                countryCode,
                city,
                networkCode,
                bound(bounds, 0),
                bound(bounds, 1),
                bound(bounds, 2),
                bound(bounds, 3),
                shipmentSize,
                dangerousGoods,
                page,
                size);
    }

    public PickupPointPageApiResponse toResponse(final PickupPointPageResult result) {
        return new PickupPointPageApiResponse(
                result.items().stream().map(this::toResponse).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages(),
                result.evaluatedAt());
    }

    public PickupPointApiResponse toResponse(final PickupPointResult result) {
        final PickupPointSnapshot point = result.pickupPoint();
        final PickupPointDepartment department = result.department();
        final List<String> reasons = new ArrayList<>();
        if (point.status() != PickupPointStatus.ACTIVE) {
            reasons.add("STATUS_" + point.status().name());
        }
        if (department == null) {
            reasons.add("DEPARTMENT_NOT_ASSIGNED");
        } else if (!department.active()) {
            reasons.add("DEPARTMENT_INACTIVE");
        }
        return new PickupPointApiResponse(
                new PickupPointIdDto(point.pickupPointId().value()),
                point.code().value(),
                point.name(),
                point.type(),
                point.status(),
                point.capabilities(),
                address(point.address()),
                coordinates(point.coordinates()),
                department(department),
                new PickupPointAvailabilityApi(reasons.isEmpty(), reasons, result.openNow()),
                point.version(),
                contact(point.contact()),
                point.accessInstructions(),
                schedule(point.openingSchedule()),
                policy(point.servicePolicy()),
                externalReference(point.externalReference()),
                point.statusReason(),
                point.createdAt(),
                point.updatedAt());
    }

    private PickupPointApiResponse toResponse(final PickupPointListItemResult result) {
        final PickupPointSearchItem point = result.pickupPoint();
        final PickupPointDepartment department = result.department();
        final List<String> reasons = availabilityReasons(point.status(), department);
        return new PickupPointApiResponse(
                new PickupPointIdDto(point.pickupPointId().value()),
                point.code(),
                point.name(),
                point.type(),
                point.status(),
                point.capabilities(),
                address(point.address()),
                coordinates(point.coordinates()),
                department(department),
                new PickupPointAvailabilityApi(reasons.isEmpty(), reasons, null),
                point.version(),
                null,
                null,
                null,
                new PickupPointServicePolicyApi(
                        point.allowedShipmentSizes(),
                        point.acceptsDangerousGoods()),
                null,
                null,
                null,
                null);
    }

    private List<String> availabilityReasons(
            final PickupPointStatus status,
            final PickupPointDepartment department) {
        final List<String> reasons = new ArrayList<>();
        if (status != PickupPointStatus.ACTIVE) {
            reasons.add("STATUS_" + status.name());
        }
        if (department == null) {
            reasons.add("DEPARTMENT_NOT_ASSIGNED");
        } else if (!department.active()) {
            reasons.add("DEPARTMENT_INACTIVE");
        }
        return reasons;
    }

    private PickupPointConfigurationCommand configuration(final PickupPointConfigurationApi request) {
        return new PickupPointConfigurationCommand(
                request.name(),
                request.type(),
                request.capabilities(),
                address(request.address()),
                departmentId(request.departmentId()),
                contact(request.contact()),
                request.accessInstructions(),
                schedule(request.openingSchedule()),
                policy(request.servicePolicy()),
                externalReference(request.externalReference()));
    }

    private PickupPointAddress address(final PickupPointAddressApi address) {
        return address == null ? null : new PickupPointAddress(
                CountryCode.valueOf(address.countryCode()),
                address.postalCode(),
                address.city(),
                address.street(),
                address.buildingNumber(),
                address.unitNumber());
    }

    private PickupPointAddressApi address(final PickupPointAddress address) {
        return address == null ? null : new PickupPointAddressApi(
                address.countryCode().name(),
                address.postalCode(),
                address.city(),
                address.street(),
                address.buildingNumber(),
                address.unitNumber());
    }

    private GeoCoordinatesApi coordinates(final GeoCoordinates coordinates) {
        return coordinates == null ? null : new GeoCoordinatesApi(coordinates.latitude(), coordinates.longitude());
    }

    private DepartmentId departmentId(final DepartmentIdDto departmentId) {
        return departmentId == null ? null : new DepartmentId(departmentId.value());
    }

    private PickupPointContact contact(final PickupPointContactApi contact) {
        return contact == null ? null : new PickupPointContact(contact.telephoneNumber(), contact.email());
    }

    private PickupPointContactApi contact(final PickupPointContact contact) {
        return contact == null ? null : new PickupPointContactApi(contact.telephoneNumber(), contact.email());
    }

    private OpeningSchedule schedule(final OpeningScheduleApi schedule) {
        return schedule == null ? null : new OpeningSchedule(
                ZoneId.of(schedule.timeZone()),
                schedule.mode(),
                list(schedule.days()).stream().map(this::openingDay).toList(),
                list(schedule.exceptions()).stream().map(this::scheduleException).toList());
    }

    private OpeningScheduleApi schedule(final OpeningSchedule schedule) {
        return schedule == null ? null : new OpeningScheduleApi(
                schedule.timeZone().getId(),
                schedule.mode(),
                schedule.days().stream().map(this::openingDay).toList(),
                schedule.exceptions().stream().map(this::scheduleException).toList());
    }

    private OpeningDay openingDay(final OpeningDayApi day) {
        return new OpeningDay(
                day.dayOfWeek(),
                day.availability(),
                list(day.intervals()).stream().map(this::interval).toList());
    }

    private OpeningDayApi openingDay(final OpeningDay day) {
        return new OpeningDayApi(
                day.dayOfWeek(),
                day.availability(),
                day.intervals().stream().map(this::interval).toList());
    }

    private OpeningScheduleException scheduleException(final OpeningScheduleExceptionApi exception) {
        return new OpeningScheduleException(
                exception.date(),
                exception.availability(),
                list(exception.intervals()).stream().map(this::interval).toList());
    }

    private OpeningScheduleExceptionApi scheduleException(final OpeningScheduleException exception) {
        return new OpeningScheduleExceptionApi(
                exception.date(),
                exception.availability(),
                exception.intervals().stream().map(this::interval).toList());
    }

    private OpeningInterval interval(final OpeningIntervalApi interval) {
        return new OpeningInterval(interval.startMinute(), interval.endMinute());
    }

    private OpeningIntervalApi interval(final OpeningInterval interval) {
        return new OpeningIntervalApi(interval.startMinute(), interval.endMinute());
    }

    private PickupPointServicePolicy policy(final PickupPointServicePolicyApi policy) {
        return policy == null ? null : new PickupPointServicePolicy(
                policy.allowedShipmentSizes(),
                policy.acceptsDangerousGoods());
    }

    private PickupPointServicePolicyApi policy(final PickupPointServicePolicy policy) {
        return policy == null ? null : new PickupPointServicePolicyApi(
                policy.allowedShipmentSizes(),
                policy.acceptsDangerousGoods());
    }

    private ExternalPointReference externalReference(final ExternalPointReferenceApi reference) {
        return reference == null ? null : new ExternalPointReference(reference.networkCode(), reference.pointCode());
    }

    private ExternalPointReferenceApi externalReference(final ExternalPointReference reference) {
        return reference == null ? null : new ExternalPointReferenceApi(
                reference.networkCode(),
                reference.pointCode());
    }

    private PickupPointDepartmentApi department(final PickupPointDepartment department) {
        return department == null ? null : new PickupPointDepartmentApi(
                new DepartmentIdDto(department.departmentId().value()),
                department.departmentCode().getValue());
    }

    private Double bound(final double[] bounds, final int index) {
        return bounds == null ? null : bounds[index];
    }

    private <T> List<T> list(final List<T> values) {
        return values == null ? List.of() : values;
    }
}
