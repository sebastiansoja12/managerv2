package com.warehouse.departmentmap.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.departmentmap.application.port.primary.command.ReserveLocationCommand;
import com.warehouse.departmentmap.application.port.primary.result.PalletLocationResult;
import com.warehouse.departmentmap.application.port.secondary.CurrentUserServicePort;
import com.warehouse.departmentmap.application.port.secondary.DepartmentMapRepository;
import com.warehouse.departmentmap.application.port.secondary.PalletMovementRepository;
import com.warehouse.departmentmap.application.port.secondary.StorageLocationRepository;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.model.DepartmentMap;
import com.warehouse.departmentmap.domain.model.PalletMovement;
import com.warehouse.departmentmap.domain.model.PalletPlacement;
import com.warehouse.departmentmap.domain.model.StorageLocation;
import com.warehouse.departmentmap.domain.vo.LocationReservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class PalletLocationPortImpl implements PalletLocationPort {

    private static final Logger log = LoggerFactory.getLogger(PalletLocationPortImpl.class);
    private static final int SEARCH_LIMIT = 50;
    private static final int MIN_QUERY_LENGTH = 2;

    private final StorageLocationRepository storageLocationRepository;
    private final DepartmentMapRepository departmentMapRepository;
    private final PalletMovementRepository palletMovementRepository;
    private final CurrentUserServicePort currentUserServicePort;

    public PalletLocationPortImpl(final StorageLocationRepository storageLocationRepository,
                                  final DepartmentMapRepository departmentMapRepository,
                                  final PalletMovementRepository palletMovementRepository,
                                  final CurrentUserServicePort currentUserServicePort) {
        this.storageLocationRepository = storageLocationRepository;
        this.departmentMapRepository = departmentMapRepository;
        this.palletMovementRepository = palletMovementRepository;
        this.currentUserServicePort = currentUserServicePort;
    }

    @Override
    @Transactional
    public PalletLocationResult placePallet(final MapElementId locationId,
                                            final PalletTrackingNumber palletTrackingNumber) {
        requirePallet(palletTrackingNumber);
        this.storageLocationRepository.findByPallet(palletTrackingNumber).ifPresent(current -> {
            throw DepartmentMapException.conflict("PALLET_ALREADY_PLACED", "Pallet " + palletTrackingNumber.value()
                    + " is already placed on location " + current.getCode() + ". Move it instead");
        });
        final StorageLocation location = loadLocation(locationId);
        final UserId userId = this.currentUserServicePort.currentUserId();
        final Instant now = Instant.now();
        final PalletPlacement placement = location.place(palletTrackingNumber, userId, now);
        final StorageLocation saved = this.storageLocationRepository.save(location);
        this.palletMovementRepository.save(PalletMovement.placed(palletTrackingNumber, saved, userId, now));
        log.info("Pallet {} placed on location {}", palletTrackingNumber.value(), saved.getCode());
        return new PalletLocationResult(placement, saved, loadMap(saved));
    }

    @Override
    @Transactional
    public PalletLocationResult movePallet(final PalletTrackingNumber palletTrackingNumber,
                                           final MapElementId targetLocationId) {
        requirePallet(palletTrackingNumber);
        final StorageLocation source = this.storageLocationRepository.findByPallet(palletTrackingNumber)
                .orElseThrow(() -> palletNotPlaced(palletTrackingNumber));
        final StorageLocation target = loadLocation(targetLocationId);
        if (source.getLocationId().equals(target.getLocationId())) {
            throw DepartmentMapException.invalid("Pallet " + palletTrackingNumber.value()
                    + " is already on location " + target.getCode());
        }
        final UserId userId = this.currentUserServicePort.currentUserId();
        final Instant now = Instant.now();
        final PalletPlacement placement = target.place(palletTrackingNumber, userId, now);
        source.remove(palletTrackingNumber);
        final StorageLocation savedSource = this.storageLocationRepository.save(source);
        final StorageLocation savedTarget = this.storageLocationRepository.save(target);
        this.palletMovementRepository.save(PalletMovement.moved(palletTrackingNumber, savedSource, savedTarget,
                userId, now));
        log.info("Pallet {} moved from {} to {}", palletTrackingNumber.value(), savedSource.getCode(),
                savedTarget.getCode());
        return new PalletLocationResult(placement, savedTarget, loadMap(savedTarget));
    }

    @Override
    @Transactional
    public void removePallet(final PalletTrackingNumber palletTrackingNumber) {
        requirePallet(palletTrackingNumber);
        final StorageLocation location = this.storageLocationRepository.findByPallet(palletTrackingNumber)
                .orElseThrow(() -> palletNotPlaced(palletTrackingNumber));
        location.remove(palletTrackingNumber);
        final StorageLocation saved = this.storageLocationRepository.save(location);
        this.palletMovementRepository.save(PalletMovement.removed(palletTrackingNumber, saved,
                this.currentUserServicePort.currentUserId(), Instant.now()));
        log.info("Pallet {} removed from location {}", palletTrackingNumber.value(), saved.getCode());
    }

    @Override
    @Transactional
    public StorageLocation reserveLocation(final MapElementId locationId, final ReserveLocationCommand command) {
        if (command == null) {
            throw DepartmentMapException.invalid("Request body is required");
        }
        final StorageLocation location = loadLocation(locationId);
        final UserId userId = this.currentUserServicePort.currentUserId();
        final Instant now = Instant.now();
        location.reserve(command.palletTrackingNumber(), command.note(), userId, now);
        final StorageLocation saved = this.storageLocationRepository.save(location);
        if (command.palletTrackingNumber() != null) {
            this.palletMovementRepository.save(PalletMovement.reserved(command.palletTrackingNumber(), saved,
                    userId, now));
        }
        log.info("Location {} reserved", saved.getCode());
        return saved;
    }

    @Override
    @Transactional
    public StorageLocation cancelReservation(final MapElementId locationId) {
        final StorageLocation location = loadLocation(locationId);
        final LocationReservation cancelled = location.cancelReservation();
        final StorageLocation saved = this.storageLocationRepository.save(location);
        cancelled.reservedPallet().ifPresent(pallet -> this.palletMovementRepository.save(
                PalletMovement.reservationCancelled(pallet, saved, this.currentUserServicePort.currentUserId(),
                        Instant.now())));
        log.info("Reservation of location {} cancelled", saved.getCode());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PalletLocationResult> findPalletLocation(final PalletTrackingNumber palletTrackingNumber) {
        requirePallet(palletTrackingNumber);
        return this.storageLocationRepository.findByPallet(palletTrackingNumber)
                .flatMap(location -> location.findPallet(palletTrackingNumber)
                        .map(placement -> new PalletLocationResult(placement, location, loadMap(location))));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PalletLocationResult> searchPallets(final String query) {
        final String fragment = query == null ? "" : query.trim().toUpperCase(Locale.ROOT);
        if (fragment.length() < MIN_QUERY_LENGTH) {
            throw DepartmentMapException.invalid("Search query must contain at least " + MIN_QUERY_LENGTH
                    + " characters");
        }
        final Map<DepartmentMapId, DepartmentMap> maps = new HashMap<>();
        return this.storageLocationRepository.findByPalletNumberFragment(fragment, SEARCH_LIMIT).stream()
                .flatMap(location -> location.getPallets().stream()
                        .filter(placement -> placement.palletTrackingNumber().value().contains(fragment))
                        .map(placement -> new PalletLocationResult(placement, location,
                                maps.computeIfAbsent(location.getDepartmentMapId(), id -> loadMap(location)))))
                .sorted(Comparator.comparing((PalletLocationResult result) ->
                                !result.placement().palletTrackingNumber().value().equals(fragment))
                        .thenComparing(result -> result.placement().palletTrackingNumber().value()))
                .limit(SEARCH_LIMIT)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PalletMovement> palletHistory(final PalletTrackingNumber palletTrackingNumber) {
        requirePallet(palletTrackingNumber);
        return this.palletMovementRepository.findHistory(palletTrackingNumber);
    }

    private StorageLocation loadLocation(final MapElementId locationId) {
        if (locationId == null || locationId.value() == null) {
            throw DepartmentMapException.invalid("Location id is required");
        }
        return this.storageLocationRepository.findById(locationId)
                .orElseThrow(() -> DepartmentMapException.notFound("LOCATION_NOT_FOUND",
                        "Storage location " + locationId.value() + " not found"));
    }

    private DepartmentMap loadMap(final StorageLocation location) {
        return this.departmentMapRepository.findById(location.getDepartmentMapId())
                .orElseThrow(() -> DepartmentMapException.notFound("MAP_NOT_FOUND",
                        "Department map " + location.getDepartmentMapId().value() + " not found"));
    }

    private static void requirePallet(final PalletTrackingNumber palletTrackingNumber) {
        if (palletTrackingNumber == null) {
            throw DepartmentMapException.invalid("Pallet number is required");
        }
    }

    private static DepartmentMapException palletNotPlaced(final PalletTrackingNumber palletTrackingNumber) {
        return DepartmentMapException.notFound("PALLET_NOT_PLACED", "Pallet " + palletTrackingNumber.value()
                + " is not placed on any map");
    }
}
