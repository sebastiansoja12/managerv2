package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.commonassets.enumeration.DeviceType;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.terminal.configuration.DeviceTestConfiguration;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.enumeration.NetworkType;
import com.warehouse.terminal.infrastructure.adapter.secondary.MobileReadRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeviceTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MobileReadRepositoryTest {

    private static final Instant BASE_TIME = Instant.parse("2026-01-11T08:00:00Z");

    @Autowired
    private MobileReadRepository repository;

    @Test
    void shouldFindMobileById() {
        repository.saveAndFlush(createMobile("MB:mobile-find-01", DeviceStatus.ACTIVE));

        final Optional<MobileEntity> found = repository.findById(new DeviceId("MB:mobile-find-01"));

        assertTrue(found.isPresent());
        assertEquals(DeviceType.MOBILE, found.get().getDeviceType());
        assertEquals(DeviceStatus.ACTIVE, found.get().getStatus());
        assertEquals("ext-MB:mobile-find-01", found.get().getExternalDeviceId().value());
    }

    @Test
    void shouldNotFindMobileByIdWhenMissing() {
        final Optional<MobileEntity> found = repository.findById(new DeviceId("MB:mobile-missing-01"));

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldFindAllMobiles() {
        repository.saveAndFlush(createMobile("MB:mobile-all-01", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createMobile("MB:mobile-all-02", DeviceStatus.BLOCKED));

        final List<MobileEntity> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(it -> "MB:mobile-all-01".equals(it.getId().value())));
        assertTrue(all.stream().anyMatch(it -> "MB:mobile-all-02".equals(it.getId().value())));
    }

    @Test
    void shouldFindExistingMobilesForFindAllById() {
        repository.saveAndFlush(createMobile("MB:mobile-bulk-01", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createMobile("MB:mobile-bulk-02", DeviceStatus.ACTIVE));

        final List<DeviceId> ids = List.of(
                new DeviceId("MB:mobile-bulk-01"),
                new DeviceId("MB:mobile-bulk-02"),
                new DeviceId("MB:mobile-bulk-missing")
        );

        final List<MobileEntity> results = StreamSupport
                .stream(repository.findAllById(ids).spliterator(), false)
                .toList();

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(it -> "MB:mobile-bulk-01".equals(it.getId().value())));
        assertTrue(results.stream().anyMatch(it -> "MB:mobile-bulk-02".equals(it.getId().value())));
    }

    @Test
    void shouldCheckMobileExistenceById() {
        repository.saveAndFlush(createMobile("MB:mobile-exists-01", DeviceStatus.ACTIVE));

        final boolean existing = repository.existsById(new DeviceId("MB:mobile-exists-01"));
        final boolean missing = repository.existsById(new DeviceId("MB:mobile-exists-02"));

        assertTrue(existing);
        assertFalse(missing);
    }

    @Test
    void shouldApplyDefaultsForMobileWhenOptionalFieldsAreMissing() {
        final MobileEntity entity = new MobileEntity();
        entity.setId(new DeviceId("MB:mobile-defaults-01"));
        entity.setExternalDeviceId(new ExternalId<>("ext-mobile-default-01"));

        repository.saveAndFlush(entity);

        final MobileEntity saved = repository.findById(new DeviceId("MB:mobile-defaults-01")).orElseThrow();

        assertEquals(DeviceType.MOBILE, saved.getDeviceType());
        assertEquals(DeviceStatus.ACTIVE, saved.getStatus());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertEquals(saved.getUpdatedAt(), saved.getLastUpdate());
        assertTrue(Boolean.TRUE.equals(saved.getActive()));
    }

    @Test
    void shouldPersistInstalledAppsForMobile() {
        final MobileEntity entity = createMobile("MB:mobile-apps-01", DeviceStatus.ACTIVE);
        entity.setInstalledApps(new HashSet<>(Set.of("com.wh.app1", "com.wh.app2", "com.wh.app3")));

        repository.saveAndFlush(entity);

        final MobileEntity saved = repository.findById(new DeviceId("MB:mobile-apps-01")).orElseThrow();

        assertEquals(3, saved.getInstalledApps().size());
        assertTrue(saved.getInstalledApps().contains("com.wh.app1"));
        assertTrue(saved.getInstalledApps().contains("com.wh.app2"));
        assertTrue(saved.getInstalledApps().contains("com.wh.app3"));
    }

    @Test
    void shouldPersistAndReadMobileEmbeddedProfiles() {
        final MobileEntity entity = createMobile("MB:mobile-embedded-01", DeviceStatus.ACTIVE);
        repository.saveAndFlush(entity);

        final MobileEntity saved = repository.findById(new DeviceId("MB:mobile-embedded-01")).orElseThrow();

        assertEquals("SN-MB:mobile-embedded-01", saved.getIdentity().getSerialNumber());
        assertEquals("MANU-MB:mobile-embedded-01", saved.getHardware().getManufacturer());
        assertEquals("android-MB:mobile-embedded-01", saved.getSoftware().getOsName());
        assertEquals(NetworkType.CELLULAR, saved.getNetwork().getNetworkType());
        assertTrue(Boolean.TRUE.equals(saved.getSecurity().getEncrypted()));
        assertEquals(50.0614, saved.getLocation().getLatitude(), 0.000001d);
        assertEquals("COURIER", saved.getOwnership().getAssignedRole());
        assertEquals(Long.valueOf(7101L), saved.getOwnership().getTeamId().value());
        assertEquals(Long.valueOf(9101L), saved.getOwnership().getVehicleId().value());
        assertEquals(Long.valueOf(2001L), saved.getUserId().value());
        assertEquals("KR1", saved.getDepartmentCode().value());
        assertEquals("2.0.0", saved.getVersion());
    }

    @Test
    void shouldUpdateExistingMobileRow() {
        repository.saveAndFlush(createMobile("MB:mobile-update-01", DeviceStatus.ACTIVE));

        final MobileEntity toUpdate = repository.findById(new DeviceId("MB:mobile-update-01")).orElseThrow();
        toUpdate.setStatus(DeviceStatus.RETIRED);
        toUpdate.setVersion("3.0.0");
        toUpdate.setInstalledApps(new HashSet<>(Set.of("com.wh.newapp")));
        toUpdate.setActive(Boolean.FALSE);
        toUpdate.setUpdatedAt(BASE_TIME.plusSeconds(300));
        toUpdate.setLastUpdate(BASE_TIME.plusSeconds(300));
        repository.saveAndFlush(toUpdate);

        final MobileEntity updated = repository.findById(new DeviceId("MB:mobile-update-01")).orElseThrow();

        assertEquals(DeviceStatus.RETIRED, updated.getStatus());
        assertEquals("3.0.0", updated.getVersion());
        assertFalse(Boolean.TRUE.equals(updated.getActive()));
        assertEquals(1, updated.getInstalledApps().size());
        assertTrue(updated.getInstalledApps().contains("com.wh.newapp"));
        assertEquals(BASE_TIME.plusSeconds(300), updated.getUpdatedAt());
    }

    @Test
    void shouldDeleteMobileById() {
        repository.saveAndFlush(createMobile("MB:mobile-delete-01", DeviceStatus.ACTIVE));
        assertTrue(repository.existsById(new DeviceId("MB:mobile-delete-01")));

        repository.deleteById(new DeviceId("MB:mobile-delete-01"));
        repository.flush();

        assertFalse(repository.existsById(new DeviceId("MB:mobile-delete-01")));
    }

    @Test
    void shouldReturnMobileCount() {
        repository.saveAndFlush(createMobile("MB:mobile-count-01", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createMobile("MB:mobile-count-02", DeviceStatus.ACTIVE));
        repository.saveAndFlush(createMobile("MB:mobile-count-03", DeviceStatus.BLOCKED));

        final long count = repository.count();

        assertEquals(3L, count);
    }

    private MobileEntity createMobile(final String id, final DeviceStatus status) {
        final MobileEntity entity = new MobileEntity();
        entity.setId(new DeviceId(id));
        entity.setExternalDeviceId(new ExternalId<>("ext-" + id));
        entity.setDeviceType(DeviceType.MOBILE);
        entity.setStatus(status);
        entity.setCreatedAt(BASE_TIME);
        entity.setUpdatedAt(BASE_TIME.plusSeconds(30));
        entity.setUserId(new UserId(2001L));
        entity.setDepartmentCode(new DepartmentCode("KR1"));
        entity.setVersion("2.0.0");
        entity.setLastUpdate(BASE_TIME.plusSeconds(60));
        entity.setActive(DeviceStatus.ACTIVE.equals(status));

        entity.setIdentity(new Identity(
                UUID.fromString("223e4567-e89b-12d3-a456-426614174000"),
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
                8192,
                128000,
                "1170x2532",
                true,
                true,
                true,
                true,
                false
        ));

        entity.setSoftware(new Software(
                "android-" + id,
                "14",
                "fw-2",
                "kernel-2",
                "boot-2",
                "2.0.0",
                "build-2",
                false,
                true
        ));

        entity.setInstalledApps(new HashSet<>(Set.of("com.wh.mobile", "com.wh.scanner")));

        entity.setNetwork(new Network(
                "10.0.1.5",
                "90.90.90.5",
                NetworkType.CELLULAR,
                "Carrier-B",
                "SIM-987",
                true,
                true,
                "CourierNet",
                "BT-44-55-66"
        ));

        entity.setSecurity(new Security(
                true,
                true,
                false,
                true,
                true,
                false,
                0,
                BASE_TIME.plusSeconds(15),
                "POLICY-2",
                "CERT-XYZ"
        ));

        entity.setLocation(new Location(
                50.0614,
                19.9366,
                220.0,
                3.1f,
                "Krakow",
                "PL-SOUTH",
                true,
                BASE_TIME.plusSeconds(20)
        ));

        entity.setOwnership(new Ownership(
                new UserId(2000L),
                new TeamId(7101L),
                new VehicleId(9101L),
                "COURIER",
                BASE_TIME,
                null
        ));

        return entity;
    }
}
