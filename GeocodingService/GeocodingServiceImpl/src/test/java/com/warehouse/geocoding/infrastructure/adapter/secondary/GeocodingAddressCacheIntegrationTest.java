package com.warehouse.geocoding.infrastructure.adapter.secondary;

import static com.warehouse.geocoding.GeocodingTestFixtures.configuration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.api.GeocodingAddressDto;
import com.warehouse.geocoding.api.GeocodingCoordinatesDto;
import com.warehouse.geocoding.application.port.primary.GeocodingCoordinatesPortImpl;
import com.warehouse.geocoding.application.port.primary.GeocodingPort;
import com.warehouse.geocoding.application.port.secondary.GeocodingProviderServicePort;
import com.warehouse.geocoding.domain.vo.Coordinates;
import com.warehouse.geocoding.domain.vo.GeocodingAddress;
import com.warehouse.geocoding.domain.vo.GeocodingConfig;
import com.warehouse.geocoding.infrastructure.adapter.primary.GeocodingCoordinatesServiceAdapter;
import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.GeocodingAddressEntity;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:geocoding;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = GeocodingAddressCacheIntegrationTest.PersistenceConfiguration.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class GeocodingAddressCacheIntegrationTest {

    @Configuration
    @EnableJpaRepositories(basePackageClasses = GeocodingAddressReadRepository.class)
    @EntityScan(basePackageClasses = GeocodingAddressEntity.class)
    static class PersistenceConfiguration {
    }

    @Autowired
    private GeocodingAddressReadRepository readRepository;

    private final CountingProvider provider = new CountingProvider();

    private GeocodingCoordinatesServiceAdapter service;

    @BeforeEach
    void setUp() {
        final GeocodingPort geocodingPort = mock(GeocodingPort.class);
        when(geocodingPort.getDefault()).thenReturn(configuration());
        service = new GeocodingCoordinatesServiceAdapter(new GeocodingCoordinatesPortImpl(geocodingPort,
                Set.of(provider), new GeocodingAddressRepositoryImpl(readRepository)));
    }

    @AfterEach
    void tearDown() {
        readRepository.deleteAll();
    }

    @Test
    void shouldStoreProviderResultAndReuseItForSameDepartmentAddress() {
        final GeocodingAddressDto departmentAddress = new GeocodingAddressDto("Katowice", "Mariacka 12", "40-001", "PL");

        final GeocodingCoordinatesDto first = service.findCoordinates(departmentAddress);
        final GeocodingCoordinatesDto second = service.findCoordinates(departmentAddress);

        assertEquals(first, second);
        assertEquals(1, provider.calls.get());
        final List<GeocodingAddressEntity> rows = readRepository.findAll();
        assertEquals(1, rows.size());
        assertEquals("katowice", rows.getFirst().getCity());
        assertEquals("mariacka", rows.getFirst().getStreet());
        assertEquals("12", rows.getFirst().getHouseNumber());
        assertEquals(GeocodingProvider.GEOAPIFY.name(), rows.getFirst().getProvider());
    }

    @Test
    void shouldReuseCachedCoordinatesForDifferentlyFormattedShipmentAddress() {
        service.findCoordinates(new GeocodingAddressDto("Katowice", "Mariacka 12", "40-001", "PL"));

        final GeocodingCoordinatesDto shipment = service.findCoordinates(
                new GeocodingAddressDto("  KATOWICE ", "mariacka    12", " 40-001", "pl"));

        assertEquals(new GeocodingCoordinatesDto(50.26, 19.02), shipment);
        assertEquals(1, provider.calls.get());
        assertEquals(1, readRepository.count());
    }

    @Test
    void shouldNotStoreEntryWhenProviderReturnsNoCoordinates() {
        provider.result = new Coordinates(null, null);

        service.findCoordinates(new GeocodingAddressDto("Nowhere", "Brak 1", "00-000", "PL"));

        assertEquals(0, readRepository.count());
    }

    @Test
    void shouldCreateSingleRowForConcurrentRequestsForSameNewAddress() throws Exception {
        final int threads = 8;
        provider.barrier = new CountDownLatch(threads);
        final GeocodingAddressDto address = new GeocodingAddressDto("Gliwice", "Zwycięstwa 1", "44-100", "PL");
        final ExecutorService executor = Executors.newFixedThreadPool(threads);
        try {
            final List<Future<GeocodingCoordinatesDto>> results = new ArrayList<>();
            for (int i = 0; i < threads; i++) {
                results.add(executor.submit(() -> service.findCoordinates(address)));
            }
            for (final Future<GeocodingCoordinatesDto> result : results) {
                assertEquals(new GeocodingCoordinatesDto(50.26, 19.02), result.get(10, TimeUnit.SECONDS));
            }
        } finally {
            executor.shutdownNow();
        }

        assertEquals(threads, provider.calls.get());
        assertEquals(1, readRepository.count());
    }

    private static final class CountingProvider implements GeocodingProviderServicePort {

        private final AtomicInteger calls = new AtomicInteger();
        private volatile Coordinates result = new Coordinates(50.26, 19.02);
        private volatile CountDownLatch barrier;

        @Override
        public boolean canHandle(final GeocodingProvider geocodingProvider) {
            return true;
        }

        @Override
        public Coordinates obtainCoordinates(final GeocodingAddress address, final GeocodingConfig config) {
            calls.incrementAndGet();
            if (barrier != null) {
                // hold every request until all of them missed the cache, so the inserts really race
                barrier.countDown();
                try {
                    barrier.await(5, TimeUnit.SECONDS);
                } catch (final InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            }
            return result;
        }
    }
}
