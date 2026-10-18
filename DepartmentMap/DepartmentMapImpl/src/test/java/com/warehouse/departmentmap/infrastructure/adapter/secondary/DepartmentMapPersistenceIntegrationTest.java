package com.warehouse.departmentmap.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.commonassets.identificator.MapElementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.departmentmap.application.port.primary.DepartmentMapPort;
import com.warehouse.departmentmap.application.port.primary.PalletLocationPort;
import com.warehouse.departmentmap.application.port.primary.command.CreateDepartmentMapCommand;
import com.warehouse.departmentmap.application.port.primary.command.ReserveLocationCommand;
import com.warehouse.departmentmap.application.port.primary.command.SaveDepartmentMapLayoutCommand;
import com.warehouse.departmentmap.application.port.primary.result.DepartmentMapView;
import com.warehouse.departmentmap.application.port.secondary.StorageLocationRepository;
import com.warehouse.departmentmap.domain.enumeration.LocationStatus;
import com.warehouse.departmentmap.domain.enumeration.MapElementType;
import com.warehouse.departmentmap.domain.enumeration.PalletMovementType;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.domain.model.MapElement;
import com.warehouse.departmentmap.domain.model.PalletMovement;
import com.warehouse.departmentmap.domain.model.StorageLocation;
import com.warehouse.departmentmap.domain.vo.ElementGeometry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.warehouse.departmentmap.infrastructure.adapter.secondary.DepartmentMapPersistenceTestConfiguration.DEPARTMENT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = DepartmentMapPersistenceTestConfiguration.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class DepartmentMapPersistenceIntegrationTest {

    @Autowired
    private DepartmentMapPort departmentMapPort;

    @Autowired
    private PalletLocationPort palletLocationPort;

    @Autowired
    private StorageLocationRepository storageLocationRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private final MapElementId singleLocationId = MapElementId.generate();
    private final MapElementId doubleLocationId = MapElementId.generate();
    private DepartmentMapId mapId;

    @BeforeEach
    void setUp() {
        this.mapId = departmentMapPort.createMap(new CreateDepartmentMapCommand(DEPARTMENT_ID, "Hall A", 60, 40, 1))
                .departmentMap().getDepartmentMapId();
        departmentMapPort.saveLayout(mapId, layout(0L, defaultElements()));
    }

    @AfterEach
    void tearDown() {
        departmentMapPort.findMaps(DEPARTMENT_ID).forEach(summary -> {
            final DepartmentMapView view = departmentMapPort.getMap(summary.departmentMap().getDepartmentMapId());
            view.locations().forEach(location -> location.getPallets()
                    .forEach(placement -> palletLocationPort.removePallet(placement.palletTrackingNumber())));
            departmentMapPort.deleteMap(summary.departmentMap().getDepartmentMapId());
        });
    }

    @Test
    void shouldPersistLayoutWithElementsInOrderAndIncrementVersion() {
        final DepartmentMapView view = departmentMapPort.getMap(mapId);

        assertEquals(1L, view.departmentMap().getVersion());
        assertEquals(List.of(MapElementType.ROOM, MapElementType.WALL, MapElementType.RACK,
                        MapElementType.STORAGE_LOCATION, MapElementType.STORAGE_LOCATION),
                view.departmentMap().getElements().stream().map(MapElement::getType).toList());
        assertEquals(45, view.departmentMap().getElements().get(2).getGeometry().rotation());
        assertEquals(2, view.locations().size());
    }

    @Test
    void shouldUpdateAndRemoveElementsAndLocations() {
        final List<MapElement> elements = new ArrayList<>(defaultElements());
        elements.remove(4);
        elements.set(3, location(singleLocationId, "Z-01", 2));

        final DepartmentMapView view = departmentMapPort.saveLayout(mapId, layout(1L, elements));

        assertEquals(2L, view.departmentMap().getVersion());
        assertEquals(4, view.departmentMap().getElements().size());
        assertEquals(1, view.locations().size());
        assertEquals("Z-01", view.locations().get(0).getCode());
        assertEquals(2, view.locations().get(0).getCapacity());
    }

    @Test
    void shouldRejectStaleLayoutVersion() {
        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> departmentMapPort.saveLayout(mapId, layout(0L, defaultElements())));

        assertEquals("MAP_VERSION_CONFLICT", exception.getCode());
    }

    @Test
    void shouldPlaceMoveAndRemovePalletWithHistory() {
        final PalletTrackingNumber pallet = new PalletTrackingNumber("PAL000000000123");

        palletLocationPort.placePallet(singleLocationId, pallet);
        palletLocationPort.movePallet(pallet, doubleLocationId);

        assertEquals("B-01", palletLocationPort.findPalletLocation(pallet).orElseThrow().location().getCode());
        assertEquals(LocationStatus.FREE, storageLocationRepository.findById(singleLocationId).orElseThrow().status());
        assertEquals(LocationStatus.PARTIALLY_OCCUPIED,
                storageLocationRepository.findById(doubleLocationId).orElseThrow().status());
        assertEquals(1, palletLocationPort.searchPallets("000123").size());

        palletLocationPort.removePallet(pallet);

        final List<PalletMovement> history = palletLocationPort.palletHistory(pallet);
        assertEquals(List.of(PalletMovementType.REMOVED, PalletMovementType.MOVED, PalletMovementType.PLACED),
                history.stream().map(PalletMovement::movementType).toList());
        assertEquals("A-01", history.get(1).fromLocation().code());
        assertNotNull(history.get(0).userId());
    }

    @Test
    void shouldPersistReservationAndBlockOtherPallet() {
        palletLocationPort.reserveLocation(singleLocationId,
                new ReserveLocationCommand(new PalletTrackingNumber("PAL9"), "Inbound"));

        final StorageLocation reserved = storageLocationRepository.findById(singleLocationId).orElseThrow();
        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> palletLocationPort.placePallet(singleLocationId, new PalletTrackingNumber("PAL1")));

        assertEquals(LocationStatus.RESERVED, reserved.status());
        assertEquals("Inbound", reserved.getReservation().orElseThrow().note());
        assertEquals("LOCATION_RESERVED", exception.getCode());
    }

    @Test
    void shouldRejectStaleLocationVersion() {
        final StorageLocation firstCopy = storageLocationRepository.findById(singleLocationId).orElseThrow();
        final StorageLocation secondCopy = storageLocationRepository.findById(singleLocationId).orElseThrow();
        final TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        transaction.executeWithoutResult(status -> {
            firstCopy.place(new PalletTrackingNumber("PAL1"), null, java.time.Instant.now());
            storageLocationRepository.save(firstCopy);
        });

        final DepartmentMapException exception = assertThrows(DepartmentMapException.class,
                () -> transaction.executeWithoutResult(status -> {
                    secondCopy.place(new PalletTrackingNumber("PAL2"), null, java.time.Instant.now());
                    storageLocationRepository.save(secondCopy);
                }));

        assertEquals("LOCATION_CONCURRENT_MODIFICATION", exception.getCode());
        assertEquals(1, storageLocationRepository.findById(singleLocationId).orElseThrow().getPallets().size());
    }

    @Test
    void shouldAllowOnlyOnePalletWhenTwoUsersOccupySameLocationConcurrently() throws Exception {
        final int attempts = 6;
        final ExecutorService executor = Executors.newFixedThreadPool(attempts);
        final CountDownLatch start = new CountDownLatch(1);
        final List<Throwable> failures = Collections.synchronizedList(new ArrayList<>());
        final List<Future<?>> futures = new ArrayList<>();
        for (int index = 0; index < attempts; index++) {
            final PalletTrackingNumber pallet = new PalletTrackingNumber("PAL-CONCURRENT-" + index);
            futures.add(executor.submit(() -> {
                start.await();
                try {
                    palletLocationPort.placePallet(singleLocationId, pallet);
                } catch (final RuntimeException exception) {
                    failures.add(exception);
                }
                return null;
            }));
        }
        start.countDown();
        for (final Future<?> future : futures) {
            future.get(30, TimeUnit.SECONDS);
        }
        executor.shutdown();

        final StorageLocation location = storageLocationRepository.findById(singleLocationId).orElseThrow();
        assertEquals(1, location.getPallets().size());
        assertEquals(attempts - 1, failures.size());
        assertTrue(failures.stream().allMatch(failure -> failure instanceof DepartmentMapException conflict
                && conflict.reason() == DepartmentMapException.Reason.CONFLICT), () -> "Unexpected failures: " + failures);
    }

    @Test
    void shouldRejectSamePalletPlacedConcurrentlyOnTwoLocations() throws Exception {
        final PalletTrackingNumber pallet = new PalletTrackingNumber("PAL-RACE");
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        final CountDownLatch start = new CountDownLatch(1);
        final List<Throwable> failures = Collections.synchronizedList(new ArrayList<>());
        final List<Future<?>> futures = new ArrayList<>();
        for (final MapElementId locationId : List.of(singleLocationId, doubleLocationId)) {
            futures.add(executor.submit(() -> {
                start.await();
                try {
                    palletLocationPort.placePallet(locationId, pallet);
                } catch (final RuntimeException exception) {
                    failures.add(exception);
                }
                return null;
            }));
        }
        start.countDown();
        for (final Future<?> future : futures) {
            future.get(30, TimeUnit.SECONDS);
        }
        executor.shutdown();

        final int placed = storageLocationRepository.findById(singleLocationId).orElseThrow().getPallets().size()
                + storageLocationRepository.findById(doubleLocationId).orElseThrow().getPallets().size();
        assertEquals(1, placed);
        assertEquals(1, failures.size());
        assertTrue(failures.get(0) instanceof DepartmentMapException, () -> "Unexpected failure: " + failures);
    }

    private List<MapElement> defaultElements() {
        return List.of(
                new MapElement(MapElementId.generate(), MapElementType.ROOM, "Hall",
                        new ElementGeometry(0, 0, 60, 40, 0), "#EEEEEE", null, null),
                new MapElement(MapElementId.generate(), MapElementType.WALL, null,
                        new ElementGeometry(0, 20, 30, 0.2, 0), null, null, null),
                new MapElement(MapElementId.generate(), MapElementType.RACK, "R1",
                        new ElementGeometry(10, 10, 8, 1, 45), null, null, null),
                location(singleLocationId, "A-01", 1),
                location(doubleLocationId, "B-01", 2));
    }

    private static MapElement location(final MapElementId locationId, final String code, final int capacity) {
        return new MapElement(locationId, MapElementType.STORAGE_LOCATION, null,
                new ElementGeometry(12, 12, 1.2, 0.8, 0), null, code, capacity);
    }

    private static SaveDepartmentMapLayoutCommand layout(final Long version, final List<MapElement> elements) {
        return new SaveDepartmentMapLayoutCommand("Hall A", 60, 40, 1, version, elements);
    }
}
