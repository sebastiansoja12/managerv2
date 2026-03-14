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
import com.warehouse.terminal.infrastructure.adapter.secondary.TerminalReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Hardware;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Identity;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Location;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Network;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Ownership;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Security;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.Software;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.TerminalEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeviceTestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TerminalReadRepositoryTest {

    private static final Instant BASE_TIME = Instant.parse("2026-01-10T10:15:30Z");

    @Autowired
    private TerminalReadRepository repository;

    @Test
    void shouldFindTerminalById() {
        final TerminalEntity entity = createTerminal("TL:terminal-find-01", DeviceStatus.ACTIVE);
        repository.saveAndFlush(entity);

        final Optional<TerminalEntity> found = repository.findById(new DeviceId("TL:terminal-find-01"));

        assertTrue(found.isPresent());
        assertEquals(DeviceType.TERMINAL, found.get().getDeviceType());
        assertEquals(DeviceStatus.ACTIVE, found.get().getStatus());
        assertEquals("ext-TL:terminal-find-01", found.get().getExternalDeviceId().value());
    }

    @Test
    void shouldNotFindTerminalByIdWhenMissing() {
        final Optional<TerminalEntity> found = repository.findById(new DeviceId("TL:terminal-missing-01"));

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldFindAllTerminals() {
        repository.saveAndFlush(createTerminal("TL:terminal-all-01", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createTerminal("TL:terminal-all-02", DeviceStatus.BLOCKED));

        final List<TerminalEntity> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(it -> "TL:terminal-all-01".equals(it.getId().value())));
        assertTrue(all.stream().anyMatch(it -> "TL:terminal-all-02".equals(it.getId().value())));
    }

    @Test
    void shouldFindExistingTerminalsForFindAllById() {
        repository.saveAndFlush(createTerminal("TL:terminal-bulk-01", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createTerminal("TL:terminal-bulk-02", DeviceStatus.ACTIVE));

        final List<DeviceId> ids = List.of(
                new DeviceId("TL:terminal-bulk-01"),
                new DeviceId("TL:terminal-bulk-02"),
                new DeviceId("TL:terminal-bulk-missing")
        );

        final List<TerminalEntity> results = StreamSupport
                .stream(repository.findAllById(ids).spliterator(), false)
                .toList();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(it -> "TL:terminal-bulk-01".equals(it.getId().value())));
        assertTrue(results.stream().anyMatch(it -> "TL:terminal-bulk-02".equals(it.getId().value())));
    }

    @Test
    void shouldCheckTerminalExistenceById() {
        repository.saveAndFlush(createTerminal("TL:terminal-exists-01", DeviceStatus.ACTIVE));

        final boolean existing = repository.existsById(new DeviceId("TL:terminal-exists-01"));
        final boolean missing = repository.existsById(new DeviceId("TL:terminal-exists-02"));

        assertTrue(existing);
        assertFalse(missing);
    }

    @Test
    void shouldApplyDefaultsForTerminalWhenOptionalFieldsAreMissing() {
        final TerminalEntity entity = new TerminalEntity();
        entity.setId(new DeviceId("TL:terminal-defaults-01"));
        entity.setExternalDeviceId(new ExternalId<>("ext-default-01"));

        repository.saveAndFlush(entity);

        final TerminalEntity saved = repository.findById(new DeviceId("TL:terminal-defaults-01")).orElseThrow();

        assertEquals(DeviceType.TERMINAL, saved.getDeviceType());
        assertEquals(DeviceStatus.ACTIVE, saved.getStatus());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertEquals(saved.getUpdatedAt(), saved.getLastUpdate());
        assertTrue(Boolean.TRUE.equals(saved.getActive()));
    }

    @Test
    void shouldApplyBlockedStatusToActiveFlagWhenActiveIsNull() {
        final TerminalEntity entity = new TerminalEntity();
        entity.setId(new DeviceId("TL:terminal-defaults-02"));
        entity.setExternalDeviceId(new ExternalId<>("ext-default-02"));
        entity.setStatus(DeviceStatus.BLOCKED);

        repository.saveAndFlush(entity);

        final TerminalEntity saved = repository.findById(new DeviceId("TL:terminal-defaults-02")).orElseThrow();

        assertEquals(DeviceStatus.BLOCKED, saved.getStatus());
        assertFalse(Boolean.TRUE.equals(saved.getActive()));
    }

    @Test
    void shouldKeepExplicitActiveFlagWhenProvided() {
        final TerminalEntity entity = new TerminalEntity();
        entity.setId(new DeviceId("TL:terminal-defaults-03"));
        entity.setExternalDeviceId(new ExternalId<>("ext-default-03"));
        entity.setStatus(DeviceStatus.BLOCKED);
        entity.setActive(Boolean.TRUE);

        repository.saveAndFlush(entity);

        final TerminalEntity saved = repository.findById(new DeviceId("TL:terminal-defaults-03")).orElseThrow();

        assertEquals(DeviceStatus.BLOCKED, saved.getStatus());
        assertTrue(Boolean.TRUE.equals(saved.getActive()));
    }

    @Test
    void shouldPersistAndReadTerminalEmbeddedProfiles() {
        final TerminalEntity entity = createTerminal("TL:terminal-embedded-01", DeviceStatus.ACTIVE);
        repository.saveAndFlush(entity);

        final TerminalEntity saved = repository.findById(new DeviceId("TL:terminal-embedded-01")).orElseThrow();

        assertEquals("SN-TL:terminal-embedded-01", saved.getIdentity().getSerialNumber());
        assertEquals("MANU-TL:terminal-embedded-01", saved.getHardware().getManufacturer());
        assertEquals("os-TL:terminal-embedded-01", saved.getSoftware().getOsName());
        assertEquals(NetworkType.WIFI, saved.getNetwork().getNetworkType());
        assertTrue(Boolean.TRUE.equals(saved.getSecurity().getEncrypted()));
        assertEquals(52.2297, saved.getLocation().getLatitude(), 0.000001d);
        assertEquals("DRIVER", saved.getOwnership().getAssignedRole());
        assertEquals(Long.valueOf(7001L), saved.getOwnership().getTeamId().value());
        assertEquals(Long.valueOf(9001L), saved.getOwnership().getVehicleId().value());
        assertEquals(Long.valueOf(1001L), saved.getUserId().value());
        assertEquals("KT1", saved.getDepartmentCode().value());
        assertEquals("1.0.0", saved.getVersion());
    }

    @Test
    void shouldUpdateExistingTerminalRow() {
        final TerminalEntity original = createTerminal("TL:terminal-update-01", DeviceStatus.ACTIVE);
        repository.saveAndFlush(original);

        final TerminalEntity toUpdate = repository.findById(new DeviceId("TL:terminal-update-01")).orElseThrow();
        toUpdate.setStatus(DeviceStatus.RETIRED);
        toUpdate.setVersion("2.0.0");
        toUpdate.setActive(Boolean.FALSE);
        toUpdate.setUpdatedAt(BASE_TIME.plusSeconds(600));
        toUpdate.setLastUpdate(BASE_TIME.plusSeconds(600));
        repository.saveAndFlush(toUpdate);

        final TerminalEntity updated = repository.findById(new DeviceId("TL:terminal-update-01")).orElseThrow();

        assertEquals(DeviceStatus.RETIRED, updated.getStatus());
        assertEquals("2.0.0", updated.getVersion());
        assertFalse(Boolean.TRUE.equals(updated.getActive()));
        assertEquals(BASE_TIME.plusSeconds(600), updated.getUpdatedAt());
    }

    @Test
    void shouldDeleteTerminalById() {
        repository.saveAndFlush(createTerminal("TL:terminal-delete-01", DeviceStatus.ACTIVE));
        assertTrue(repository.existsById(new DeviceId("TL:terminal-delete-01")));

        repository.deleteById(new DeviceId("TL:terminal-delete-01"));
        repository.flush();

        assertFalse(repository.existsById(new DeviceId("TL:terminal-delete-01")));
    }

    @Test
    void shouldReturnTerminalCount() {
        repository.saveAndFlush(createTerminal("TL:terminal-count-01", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createTerminal("TL:terminal-count-02", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createTerminal("TL:terminal-count-03", DeviceStatus.BLOCKED));

        final long count = repository.count();

        assertEquals(3L, count);
    }

    private TerminalEntity createTerminal(final String id, final DeviceStatus status) {
        final TerminalEntity entity = new TerminalEntity();
        entity.setId(new DeviceId(id));
        entity.setExternalDeviceId(new ExternalId<>("ext-" + id));
        entity.setDeviceType(DeviceType.TERMINAL);
        entity.setStatus(status);
        entity.setCreatedAt(BASE_TIME);
        entity.setUpdatedAt(BASE_TIME.plusSeconds(60));
        entity.setUserId(new UserId(1001L));
        entity.setDepartmentCode(new DepartmentCode("KT1"));
        entity.setVersion("1.0.0");
        entity.setLastUpdate(BASE_TIME.plusSeconds(120));
        entity.setActive(DeviceStatus.ACTIVE.equals(status));

        final Identity identity = new Identity(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                "SN-" + id,
                "IMEI-" + id,
                "MAC-" + id,
                "ASSET-" + id,
                "BAR-" + id,
                "EXTSYS-" + id,
                "MDM-" + id
        );

        final Hardware hardware = new Hardware(
                "MANU-" + id,
                "MODEL-" + id,
                "PRODUCT-" + id,
                "CPU-" + id,
                4096,
                64000,
                "1080x1920",
                true,
                true,
                true,
                true,
                true
        );

        final Software software = new Software(
                "os-" + id,
                "1.0",
                "fw-1",
                "kernel-1",
                "boot-1",
                "1.0.0",
                "build-1",
                false,
                true
        );

        final Network network = new Network(
                "10.0.0.1",
                "80.80.80.1",
                NetworkType.WIFI,
                "Carrier-A",
                "SIM-123",
                false,
                true,
                "Warehouse-WiFi",
                "BT-11-22-33"
        );

        final Security security = new Security(
                true,
                true,
                false,
                true,
                true,
                false,
                1,
                BASE_TIME.plusSeconds(30),
                "POLICY-1",
                "CERT-ABC"
        );

        final Location location = new Location(
                52.2297,
                21.0122,
                100.1,
                5.5f,
                "Warsaw",
                "PL-CENTER",
                true,
                BASE_TIME.plusSeconds(45)
        );

        final Ownership ownership = new Ownership(
                new UserId(1000L),
                new TeamId(7001L),
                new VehicleId(9001L),
                "DRIVER",
                BASE_TIME,
                null
        );

        entity.setIdentity(identity);
        entity.setHardware(hardware);
        entity.setSoftware(software);
        entity.setNetwork(network);
        entity.setSecurity(security);
        entity.setLocation(location);
        entity.setOwnership(ownership);

        return entity;
    }
}
