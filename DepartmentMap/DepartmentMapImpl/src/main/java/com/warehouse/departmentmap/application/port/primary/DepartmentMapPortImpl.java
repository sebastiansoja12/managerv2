package com.warehouse.departmentmap.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.departmentmap.application.port.primary.command.CreateDepartmentMapCommand;
import com.warehouse.departmentmap.application.port.primary.command.SaveDepartmentMapLayoutCommand;
import com.warehouse.departmentmap.application.port.primary.result.DepartmentMapSummary;
import com.warehouse.departmentmap.application.port.primary.result.DepartmentMapView;
import com.warehouse.departmentmap.application.port.secondary.CurrentUserServicePort;
import com.warehouse.departmentmap.application.port.secondary.DepartmentMapRepository;
import com.warehouse.departmentmap.application.port.secondary.DepartmentServicePort;
import com.warehouse.departmentmap.application.port.secondary.StorageLocationRepository;
import com.warehouse.departmentmap.domain.enumeration.LocationStatus;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.model.DepartmentMap;
import com.warehouse.departmentmap.domain.model.StorageLocation;
import com.warehouse.departmentmap.domain.service.StorageLocationSynchronizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

public class DepartmentMapPortImpl implements DepartmentMapPort {

    private static final Logger log = LoggerFactory.getLogger(DepartmentMapPortImpl.class);

    private final DepartmentMapRepository departmentMapRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final DepartmentServicePort departmentServicePort;
    private final CurrentUserServicePort currentUserServicePort;
    private final StorageLocationSynchronizer storageLocationSynchronizer;

    public DepartmentMapPortImpl(final DepartmentMapRepository departmentMapRepository,
                                 final StorageLocationRepository storageLocationRepository,
                                 final DepartmentServicePort departmentServicePort,
                                 final CurrentUserServicePort currentUserServicePort,
                                 final StorageLocationSynchronizer storageLocationSynchronizer) {
        this.departmentMapRepository = departmentMapRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.departmentServicePort = departmentServicePort;
        this.currentUserServicePort = currentUserServicePort;
        this.storageLocationSynchronizer = storageLocationSynchronizer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentMapSummary> findMaps(final DepartmentId departmentId) {
        return this.departmentMapRepository.findByDepartment(departmentId).stream()
                .map(this::summary)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentMapView getMap(final DepartmentMapId departmentMapId) {
        final DepartmentMap departmentMap = loadMap(departmentMapId);
        return new DepartmentMapView(departmentMap, this.storageLocationRepository.findByMap(departmentMapId));
    }

    @Override
    @Transactional
    public DepartmentMapView createMap(final CreateDepartmentMapCommand command) {
        if (command == null) {
            throw DepartmentMapException.invalid("Request body is required");
        }
        if (command.departmentId() == null || !this.departmentServicePort.departmentExists(command.departmentId())) {
            throw DepartmentMapException.notFound("DEPARTMENT_NOT_FOUND", "Department "
                    + (command.departmentId() == null ? "" : command.departmentId().getValue()) + " does not exist");
        }
        final DepartmentMap departmentMap = this.departmentMapRepository.save(DepartmentMap.create(
                this.currentUserServicePort.currentOperatorId(), command.departmentId(), command.name(),
                command.width(), command.height(), command.gridSize(), Instant.now()));
        log.info("Department map {} created for department {}", departmentMap.getDepartmentMapId().value(),
                departmentMap.getDepartmentId().getValue());
        return new DepartmentMapView(departmentMap, List.of());
    }

    @Override
    @Transactional
    public DepartmentMapView saveLayout(final DepartmentMapId departmentMapId,
                                        final SaveDepartmentMapLayoutCommand command) {
        if (command == null) {
            throw DepartmentMapException.invalid("Request body is required");
        }
        final DepartmentMap departmentMap = loadMap(departmentMapId);
        departmentMap.verifyVersion(command.version());
        departmentMap.changeLayout(command.name(), command.width(), command.height(), command.gridSize(),
                command.elements(), Instant.now());
        final StorageLocationSynchronizer.Plan plan = this.storageLocationSynchronizer.plan(departmentMap,
                this.storageLocationRepository.findByMap(departmentMapId));
        final DepartmentMap savedMap = this.departmentMapRepository.save(departmentMap);
        plan.removed().forEach(this.storageLocationRepository::delete);
        plan.changed().forEach(this.storageLocationRepository::save);
        plan.created().forEach(this.storageLocationRepository::save);
        log.info("Department map {} layout saved: {} elements, {} locations created, {} changed, {} removed",
                departmentMapId.value(), departmentMap.getElements().size(), plan.created().size(),
                plan.changed().size(), plan.removed().size());
        return new DepartmentMapView(savedMap, this.storageLocationRepository.findByMap(departmentMapId));
    }

    @Override
    @Transactional
    public void deleteMap(final DepartmentMapId departmentMapId) {
        final DepartmentMap departmentMap = loadMap(departmentMapId);
        final List<StorageLocation> locations = this.storageLocationRepository.findByMap(departmentMapId);
        if (locations.stream().anyMatch(location -> !location.isEmpty())) {
            throw DepartmentMapException.conflict("MAP_NOT_EMPTY", "Map " + departmentMap.getName()
                    + " still holds pallets. Move or remove them before deleting the map");
        }
        locations.forEach(this.storageLocationRepository::delete);
        this.departmentMapRepository.delete(departmentMapId);
        log.info("Department map {} deleted", departmentMapId.value());
    }

    private DepartmentMapSummary summary(final DepartmentMap departmentMap) {
        final List<StorageLocation> locations = this.storageLocationRepository.findByMap(
                departmentMap.getDepartmentMapId());
        final int occupied = (int) locations.stream()
                .filter(location -> location.status() == LocationStatus.OCCUPIED)
                .count();
        final int pallets = locations.stream().mapToInt(location -> location.getPallets().size()).sum();
        return new DepartmentMapSummary(departmentMap, locations.size(), occupied, pallets);
    }

    private DepartmentMap loadMap(final DepartmentMapId departmentMapId) {
        if (departmentMapId == null || departmentMapId.value() == null) {
            throw DepartmentMapException.invalid("Map id is required");
        }
        return this.departmentMapRepository.findById(departmentMapId)
                .orElseThrow(() -> DepartmentMapException.notFound("MAP_NOT_FOUND",
                        "Department map " + departmentMapId.value() + " not found"));
    }
}
