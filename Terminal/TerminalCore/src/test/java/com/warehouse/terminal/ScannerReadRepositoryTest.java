package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.TeamId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.terminal.configuration.DeviceTestConfiguration;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.enumeration.NetworkType;
import com.warehouse.terminal.domain.model.device.Scanner;
import com.warehouse.terminal.infrastructure.adapter.secondary.ScannerReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Hardware;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Identity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Network;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Ownership;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.ScannerEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeviceTestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ScannerReadRepositoryTest {

    private static final Instant BASE_TIME = Instant.parse("2026-01-12T06:00:00Z");

    @Autowired
    private ScannerReadRepository repository;

    @Test
    void shouldFindScannerById() {
        repository.saveAndFlush(createScanner("SC:scanner-find-01", DeviceStatus.ACTIVE));

        final Optional<ScannerEntity> found = repository.findById(new DeviceId("SC:scanner-find-01"));

        assertTrue(found.isPresent());
        assertEquals(DeviceType.SCANNER, found.get().getDeviceType());
        assertEquals(DeviceStatus.ACTIVE, found.get().getStatus());
        assertEquals("ext-SC:scanner-find-01", found.get().getExternalDeviceId().value());
    }

    @Test
    void shouldNotFindScannerByIdWhenMissing() {
        final Optional<ScannerEntity> found = repository.findById(new DeviceId("SC:scanner-missing-01"));

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldFindAllScanners() {
        repository.saveAndFlush(createScanner("SC:scanner-all-01", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createScanner("SC:scanner-all-02", DeviceStatus.BLOCKED));

        final List<ScannerEntity> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(it -> "SC:scanner-all-01".equals(it.getId().value())));
        assertTrue(all.stream().anyMatch(it -> "SC:scanner-all-02".equals(it.getId().value())));
    }

    @Test
    void shouldFindExistingScannersForFindAllById() {
        repository.saveAndFlush(createScanner("SC:scanner-bulk-01", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createScanner("SC:scanner-bulk-02", DeviceStatus.ACTIVE));

        final List<DeviceId> ids = List.of(
                new DeviceId("SC:scanner-bulk-01"),
                new DeviceId("SC:scanner-bulk-02"),
                new DeviceId("SC:scanner-bulk-missing")
        );

        final List<ScannerEntity> results = StreamSupport
                .stream(repository.findAllById(ids).spliterator(), false)
                .toList();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(it -> "SC:scanner-bulk-01".equals(it.getId().value())));
        assertTrue(results.stream().anyMatch(it -> "SC:scanner-bulk-02".equals(it.getId().value())));
    }

    @Test
    void shouldCheckScannerExistenceById() {
        repository.saveAndFlush(createScanner("SC:scanner-exists-01", DeviceStatus.ACTIVE));

        final boolean existing = repository.existsById(new DeviceId("SC:scanner-exists-01"));
        final boolean missing = repository.existsById(new DeviceId("SC:scanner-exists-02"));

        assertTrue(existing);
        assertFalse(missing);
    }

    @Test
    void shouldApplyDefaultsForScannerWhenOptionalFieldsAreMissing() {
        final ScannerEntity entity = new ScannerEntity();
        entity.setId(new DeviceId("SC:scanner-defaults-01"));
        entity.setExternalDeviceId(new ExternalId<>("ext-scanner-default-01"));

        repository.saveAndFlush(entity);

        final ScannerEntity saved = repository.findById(new DeviceId("SC:scanner-defaults-01")).orElseThrow();

        assertEquals(DeviceType.SCANNER, saved.getDeviceType());
        assertEquals(DeviceStatus.ACTIVE, saved.getStatus());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
    }

    @Test
    void shouldPersistScannerSpecificEnums() {
        final ScannerEntity entity = createScanner("SC:scanner-types-01", DeviceStatus.ACTIVE);
        entity.setScanType(Scanner.ScanType.QRCODE);
        entity.setScannerType(Scanner.ScannerType.RING);

        repository.saveAndFlush(entity);

        final ScannerEntity saved = repository.findById(new DeviceId("SC:scanner-types-01")).orElseThrow();

        assertEquals(Scanner.ScanType.QRCODE, saved.getScanType());
        assertEquals(Scanner.ScannerType.RING, saved.getScannerType());
    }

    @Test
    void shouldPersistAndReadScannerEmbeddedProfiles() {
        final ScannerEntity entity = createScanner("SC:scanner-embedded-01", DeviceStatus.ACTIVE);
        repository.saveAndFlush(entity);

        final ScannerEntity saved = repository.findById(new DeviceId("SC:scanner-embedded-01")).orElseThrow();

        assertEquals("SN-SC:scanner-embedded-01", saved.getIdentity().getSerialNumber());
        assertEquals("MANU-SC:scanner-embedded-01", saved.getHardware().getManufacturer());
        assertEquals(NetworkType.ETHERNET, saved.getNetwork().getNetworkType());
        assertEquals("PICKER", saved.getOwnership().getAssignedRole());
        assertEquals(Long.valueOf(7201L), saved.getOwnership().getTeamId().value());
        assertEquals(Long.valueOf(9201L), saved.getOwnership().getVehicleId().value());
        assertEquals(Long.valueOf(3001L), saved.getUserId().value());
        assertEquals("WR1", saved.getDepartmentCode().value());
    }

    @Test
    void shouldUpdateExistingScannerRow() {
        repository.saveAndFlush(createScanner("SC:scanner-update-01", DeviceStatus.ACTIVE));

        final ScannerEntity toUpdate = repository.findById(new DeviceId("SC:scanner-update-01")).orElseThrow();
        toUpdate.setStatus(DeviceStatus.RETIRED);
        toUpdate.setUpdatedAt(BASE_TIME.plusSeconds(500));
        toUpdate.setScanType(Scanner.ScanType.BARCODE);
        toUpdate.setScannerType(Scanner.ScannerType.GLOVE);
        repository.saveAndFlush(toUpdate);

        final ScannerEntity updated = repository.findById(new DeviceId("SC:scanner-update-01")).orElseThrow();

        assertEquals(DeviceStatus.RETIRED, updated.getStatus());
        assertEquals(Scanner.ScanType.BARCODE, updated.getScanType());
        assertEquals(Scanner.ScannerType.GLOVE, updated.getScannerType());
        assertEquals(BASE_TIME.plusSeconds(500), updated.getUpdatedAt());
    }

    @Test
    void shouldDeleteScannerById() {
        repository.saveAndFlush(createScanner("SC:scanner-delete-01", DeviceStatus.ACTIVE));
        assertTrue(repository.existsById(new DeviceId("SC:scanner-delete-01")));

        repository.deleteById(new DeviceId("SC:scanner-delete-01"));
        repository.flush();

        assertFalse(repository.existsById(new DeviceId("SC:scanner-delete-01")));
    }

    @Test
    void shouldReturnScannerCount() {
        repository.saveAndFlush(createScanner("SC:scanner-count-01", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createScanner("SC:scanner-count-02", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createScanner("SC:scanner-count-03", DeviceStatus.BLOCKED));

        final long count = repository.count();

        assertEquals(3L, count);
    }

    private ScannerEntity createScanner(final String id, final DeviceStatus status) {
        final ScannerEntity entity = new ScannerEntity();
        entity.setId(new DeviceId(id));
        entity.setExternalDeviceId(new ExternalId<>("ext-" + id));
        entity.setDeviceType(DeviceType.SCANNER);
        entity.setStatus(status);
        entity.setCreatedAt(BASE_TIME);
        entity.setUpdatedAt(BASE_TIME.plusSeconds(20));
        entity.setUserId(new UserId(3001L));
        entity.setDepartmentCode(new DepartmentCode("WR1"));

        entity.setIdentity(new Identity(
                UUID.fromString("323e4567-e89b-12d3-a456-426614174000"),
                "SN-" + id,
                "IMEI-" + id,
                "MAC-" + id,
                "ASSET-" + id,
                "BAR-" + id,
                "EXTSYS-" + id,
                "MDM-" + id
        ));

        entity.setHardware(new Hardware(
                "MANU-" + id,
                "MODEL-" + id,
                "PRODUCT-" + id,
                "CPU-" + id,
                2048,
                32000,
                "800x600",
                true,
                false,
                false,
                false,
                true
        ));

        entity.setNetwork(new Network(
                "172.16.0.5",
                "91.91.91.5",
                NetworkType.ETHERNET,
                "Carrier-C",
                "SIM-555",
                false,
                false,
                "Warehouse-LAN",
                "BT-77-88-99"
        ));

        entity.setOwnership(new Ownership(
                new UserId(3000L),
                new TeamId(7201L),
                new VehicleId(9201L),
                "PICKER",
                BASE_TIME,
                null
        ));

        entity.setScanType(Scanner.ScanType.BARCODE);
        entity.setScannerType(Scanner.ScannerType.HANDHELD);

        return entity;
    }
}
