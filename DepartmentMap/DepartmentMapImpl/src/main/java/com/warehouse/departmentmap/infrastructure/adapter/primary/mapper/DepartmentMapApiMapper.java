package com.warehouse.departmentmap.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.departmentmap.api.dto.PalletLocationDto;
import com.warehouse.departmentmap.application.port.primary.command.CreateDepartmentMapCommand;
import com.warehouse.departmentmap.application.port.primary.command.ReserveLocationCommand;
import com.warehouse.departmentmap.application.port.primary.command.SaveDepartmentMapLayoutCommand;
import com.warehouse.departmentmap.application.port.primary.result.DepartmentMapSummary;
import com.warehouse.departmentmap.application.port.primary.result.DepartmentMapView;
import com.warehouse.departmentmap.application.port.primary.result.PalletLocationResult;
import com.warehouse.departmentmap.domain.enumeration.MapElementType;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.model.DepartmentMap;
import com.warehouse.departmentmap.domain.model.MapElement;
import com.warehouse.departmentmap.domain.model.PalletMovement;
import com.warehouse.departmentmap.domain.model.PalletPlacement;
import com.warehouse.departmentmap.domain.model.StorageLocation;
import com.warehouse.departmentmap.domain.vo.ElementGeometry;
import com.warehouse.departmentmap.domain.vo.LocationReservation;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.CreateDepartmentMapApiRequest;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.DepartmentMapApiResponse;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.DepartmentMapSummaryApiResponse;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.LocationReservationApiResponse;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.MapElementApi;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.PalletLocationApiResponse;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.PalletMovementApiResponse;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.PalletPlacementApiResponse;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.ReserveLocationApiRequest;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.SaveDepartmentMapLayoutApiRequest;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.StorageLocationApiResponse;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class DepartmentMapApiMapper {

    private static final double DEFAULT_GRID_SIZE = 1;

    private DepartmentMapApiMapper() {
    }

    public static CreateDepartmentMapCommand map(final CreateDepartmentMapApiRequest request) {
        if (request == null) {
            throw DepartmentMapException.invalid("Request body is required");
        }
        if (request.departmentId() == null) {
            throw DepartmentMapException.invalid("Department id is required");
        }
        return new CreateDepartmentMapCommand(new DepartmentId(request.departmentId()), request.name(),
                required(request.width(), "width"), required(request.height(), "height"),
                request.gridSize() == null ? DEFAULT_GRID_SIZE : request.gridSize());
    }

    public static SaveDepartmentMapLayoutCommand map(final SaveDepartmentMapLayoutApiRequest request) {
        if (request == null) {
            throw DepartmentMapException.invalid("Request body is required");
        }
        final List<MapElement> elements = request.elements() == null ? List.of()
                : request.elements().stream().map(DepartmentMapApiMapper::map).toList();
        return new SaveDepartmentMapLayoutCommand(request.name(), required(request.width(), "width"),
                required(request.height(), "height"),
                request.gridSize() == null ? DEFAULT_GRID_SIZE : request.gridSize(), request.version(), elements);
    }

    public static ReserveLocationCommand map(final ReserveLocationApiRequest request) {
        if (request == null) {
            return new ReserveLocationCommand(null, null);
        }
        return new ReserveLocationCommand(optionalPallet(request.palletTrackingNumber()), request.note());
    }

    public static PalletTrackingNumber pallet(final String value) {
        if (value == null || value.isBlank()) {
            throw DepartmentMapException.invalid("Pallet number is required");
        }
        final String normalized = value.trim();
        if (normalized.length() > 64) {
            throw DepartmentMapException.invalid("Pallet number must not exceed 64 characters");
        }
        return new PalletTrackingNumber(normalized);
    }

    public static DepartmentMapId mapId(final String value) {
        return new DepartmentMapId(uuid(value, "map id"));
    }

    public static MapElementId locationId(final String value) {
        return new MapElementId(uuid(value, "location id"));
    }

    public static DepartmentMapApiResponse map(final DepartmentMapView view) {
        final DepartmentMap departmentMap = view.departmentMap();
        return new DepartmentMapApiResponse(departmentMap.getDepartmentMapId().value(),
                departmentMap.getDepartmentId().getValue(), departmentMap.getName(), departmentMap.getWidth(),
                departmentMap.getHeight(), departmentMap.getGridSize(), departmentMap.getVersion(),
                departmentMap.getCreatedAt(), departmentMap.getUpdatedAt(),
                departmentMap.getElements().stream().map(DepartmentMapApiMapper::map).toList(),
                view.locations().stream().map(DepartmentMapApiMapper::map).toList());
    }

    public static DepartmentMapSummaryApiResponse map(final DepartmentMapSummary summary) {
        final DepartmentMap departmentMap = summary.departmentMap();
        return new DepartmentMapSummaryApiResponse(departmentMap.getDepartmentMapId().value(),
                departmentMap.getDepartmentId().getValue(), departmentMap.getName(), departmentMap.getWidth(),
                departmentMap.getHeight(), departmentMap.getVersion(), departmentMap.getUpdatedAt(),
                summary.locationCount(), summary.occupiedLocationCount(), summary.palletCount());
    }

    public static StorageLocationApiResponse map(final StorageLocation location) {
        return new StorageLocationApiResponse(location.getLocationId().value(),
                location.getDepartmentMapId().value(), location.getCode(), location.getCapacity(),
                location.status().name(),
                location.getPallets().stream().map(DepartmentMapApiMapper::map).toList(),
                location.getReservation().map(DepartmentMapApiMapper::map).orElse(null),
                location.getVersion());
    }

    public static PalletLocationApiResponse map(final PalletLocationResult result) {
        return new PalletLocationApiResponse(result.placement().palletTrackingNumber().value(),
                result.departmentMap().getDepartmentMapId().value(), result.departmentMap().getName(),
                result.departmentMap().getDepartmentId().getValue(), result.location().getLocationId().value(),
                result.location().getCode(), userId(result.placement().placedBy()), result.placement().placedAt());
    }

    public static PalletLocationDto toDto(final PalletLocationResult result) {
        return new PalletLocationDto(result.placement().palletTrackingNumber().value(),
                result.departmentMap().getDepartmentMapId().value(), result.departmentMap().getName(),
                result.departmentMap().getDepartmentId().getValue(), result.location().getLocationId().value(),
                result.location().getCode(), result.placement().placedAt());
    }

    public static PalletMovementApiResponse map(final PalletMovement movement) {
        return new PalletMovementApiResponse(movement.movementId().value(), movement.palletTrackingNumber().value(),
                movement.movementType().name(), movement.departmentMapId().value(),
                movement.fromLocation() == null ? null : movement.fromLocation().locationId().value(),
                movement.fromLocation() == null ? null : movement.fromLocation().code(),
                movement.toLocation() == null ? null : movement.toLocation().locationId().value(),
                movement.toLocation() == null ? null : movement.toLocation().code(),
                userId(movement.userId()), movement.occurredAt());
    }

    private static MapElement map(final MapElementApi element) {
        if (element == null) {
            throw DepartmentMapException.invalid("Map element must not be empty");
        }
        final MapElementId elementId = element.elementId() == null || element.elementId().isBlank()
                ? MapElementId.generate()
                : new MapElementId(uuid(element.elementId(), "element id"));
        return new MapElement(elementId, elementType(element.type()), element.label(),
                new ElementGeometry(required(element.x(), "x"), required(element.y(), "y"),
                        required(element.width(), "width"), required(element.height(), "height"),
                        element.rotation() == null ? 0 : element.rotation()),
                element.color(), element.locationCode(), element.capacity());
    }

    private static MapElementApi map(final MapElement element) {
        final ElementGeometry geometry = element.getGeometry();
        return new MapElementApi(element.getElementId().value().toString(), element.getType().name(),
                element.getLabel(), geometry.x(), geometry.y(), geometry.width(), geometry.height(),
                geometry.rotation(), element.getColor(), element.getLocationCode(), element.getCapacity());
    }

    private static PalletPlacementApiResponse map(final PalletPlacement placement) {
        return new PalletPlacementApiResponse(placement.palletTrackingNumber().value(), userId(placement.placedBy()),
                placement.placedAt());
    }

    private static LocationReservationApiResponse map(final LocationReservation reservation) {
        return new LocationReservationApiResponse(
                reservation.reservedPallet().map(PalletTrackingNumber::value).orElse(null), reservation.note(),
                userId(reservation.reservedBy()), reservation.reservedAt());
    }

    private static MapElementType elementType(final String value) {
        if (value == null || value.isBlank()) {
            throw DepartmentMapException.invalid("Element type is required");
        }
        try {
            return MapElementType.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (final IllegalArgumentException exception) {
            throw DepartmentMapException.invalid("Unknown element type " + value);
        }
    }

    private static PalletTrackingNumber optionalPallet(final String value) {
        return value == null || value.isBlank() ? null : pallet(value);
    }

    private static Long userId(final UserId userId) {
        return userId == null ? null : userId.value();
    }

    private static double required(final Double value, final String field) {
        if (value == null) {
            throw DepartmentMapException.invalid("Field " + field + " is required");
        }
        return value;
    }

    private static UUID uuid(final String value, final String field) {
        if (value == null || value.isBlank()) {
            throw DepartmentMapException.invalid("Field " + field + " is required");
        }
        try {
            return UUID.fromString(value.trim());
        } catch (final IllegalArgumentException exception) {
            throw DepartmentMapException.invalid("Field " + field + " must be a valid UUID");
        }
    }
}
