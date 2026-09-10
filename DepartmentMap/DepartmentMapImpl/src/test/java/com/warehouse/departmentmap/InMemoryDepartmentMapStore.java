package com.warehouse.departmentmap;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.departmentmap.application.port.secondary.CurrentUserServicePort;
import com.warehouse.departmentmap.application.port.secondary.DepartmentMapRepository;
import com.warehouse.departmentmap.application.port.secondary.DepartmentServicePort;
import com.warehouse.departmentmap.application.port.secondary.PalletMovementRepository;
import com.warehouse.departmentmap.application.port.secondary.StorageLocationRepository;
import com.warehouse.departmentmap.domain.model.DepartmentMap;
import com.warehouse.departmentmap.domain.model.PalletMovement;
import com.warehouse.departmentmap.domain.model.StorageLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class InMemoryDepartmentMapStore implements DepartmentMapRepository, StorageLocationRepository,
        PalletMovementRepository, DepartmentServicePort, CurrentUserServicePort {

    private final Map<DepartmentMapId, DepartmentMap> maps = new LinkedHashMap<>();
    private final Map<MapElementId, StorageLocation> locations = new LinkedHashMap<>();
    private final List<PalletMovement> movements = new ArrayList<>();
    private final Set<DepartmentId> departments;
    private final OperatorId operatorId;
    private final UserId userId;

    public InMemoryDepartmentMapStore(final Set<DepartmentId> departments, final OperatorId operatorId, final UserId userId) {
        this.departments = departments;
        this.operatorId = operatorId;
        this.userId = userId;
    }

    @Override
    public DepartmentMap save(final DepartmentMap departmentMap) {
        final long nextVersion = departmentMap.getVersion() == null ? 0 : departmentMap.getVersion() + 1;
        final DepartmentMap saved = new DepartmentMap(departmentMap.getDepartmentMapId(), departmentMap.getOperatorId(),
                departmentMap.getDepartmentId(), departmentMap.getName(), departmentMap.getWidth(),
                departmentMap.getHeight(), departmentMap.getGridSize(), departmentMap.getElements(), nextVersion,
                departmentMap.getCreatedAt(), departmentMap.getUpdatedAt());
        maps.put(saved.getDepartmentMapId(), saved);
        return saved;
    }

    @Override
    public Optional<DepartmentMap> findById(final DepartmentMapId departmentMapId) {
        return Optional.ofNullable(maps.get(departmentMapId));
    }

    @Override
    public List<DepartmentMap> findByDepartment(final DepartmentId departmentId) {
        return maps.values().stream().filter(map -> map.getDepartmentId().equals(departmentId)).toList();
    }

    @Override
    public void delete(final DepartmentMapId departmentMapId) {
        maps.remove(departmentMapId);
    }

    @Override
    public StorageLocation save(final StorageLocation storageLocation) {
        locations.put(storageLocation.getLocationId(), copy(storageLocation));
        return storageLocation;
    }

    @Override
    public Optional<StorageLocation> findById(final MapElementId locationId) {
        return Optional.ofNullable(locations.get(locationId)).map(InMemoryDepartmentMapStore::copy);
    }

    @Override
    public List<StorageLocation> findByMap(final DepartmentMapId departmentMapId) {
        return locations.values().stream()
                .filter(location -> location.getDepartmentMapId().equals(departmentMapId))
                .map(InMemoryDepartmentMapStore::copy)
                .toList();
    }

    @Override
    public Optional<StorageLocation> findByPallet(final PalletTrackingNumber palletTrackingNumber) {
        return locations.values().stream()
                .filter(location -> location.holds(palletTrackingNumber))
                .findFirst()
                .map(InMemoryDepartmentMapStore::copy);
    }

    @Override
    public List<StorageLocation> findByPalletNumberFragment(final String fragment, final int limit) {
        return locations.values().stream()
                .filter(location -> location.getPallets().stream()
                        .anyMatch(placement -> placement.palletTrackingNumber().value().contains(fragment)))
                .limit(limit)
                .map(InMemoryDepartmentMapStore::copy)
                .toList();
    }

    @Override
    public void delete(final StorageLocation storageLocation) {
        locations.remove(storageLocation.getLocationId());
    }

    @Override
    public void save(final PalletMovement palletMovement) {
        movements.add(palletMovement);
    }

    @Override
    public List<PalletMovement> findHistory(final PalletTrackingNumber palletTrackingNumber) {
        return movements.stream()
                .filter(movement -> Objects.equals(movement.palletTrackingNumber(), palletTrackingNumber))
                .sorted(Comparator.comparing(PalletMovement::occurredAt).reversed())
                .toList();
    }

    @Override
    public boolean departmentExists(final DepartmentId departmentId) {
        return departments.contains(departmentId);
    }

    @Override
    public OperatorId currentOperatorId() {
        return operatorId;
    }

    @Override
    public UserId currentUserId() {
        return userId;
    }

    public List<PalletMovement> movements() {
        return List.copyOf(movements);
    }

    public int locationCount() {
        return locations.size();
    }

    private static StorageLocation copy(final StorageLocation location) {
        return new StorageLocation(location.getLocationId(), location.getDepartmentMapId(), location.getOperatorId(),
                location.getCode(), location.getCapacity(), location.getReservation().orElse(null),
                location.getPallets(), location.getVersion());
    }
}
