package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.mail.domain.service.MailService;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import com.warehouse.shipment.domain.listener.ShipmentEventListener;
import com.warehouse.shipment.domain.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.domain.port.secondary.PriceRepository;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.port.secondary.SignatureRepository;
import com.warehouse.shipment.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.shipment.infrastructure.adapter.primary.ShipmentInternalController;
import com.warehouse.shipment.infrastructure.adapter.secondary.ExternalFeignClient;
import com.warehouse.shipment.infrastructure.adapter.secondary.PriceReadRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.PriceRepositoryImpl;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentReadRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.tools.returning.ReturnProperties;
import com.warehouse.voronoi.VoronoiService;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = ShipmentReadRepositoryTest.ShipmentReadRepositoryTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/shipment.xml")
public class ShipmentReadRepositoryTest {

    @EntityScan(basePackages = { "com.warehouse.shipment"})
    @EnableJpaRepositories(basePackages = { "com.warehouse.shipment.infrastructure.adapter.secondary"})
    public static class ShipmentReadRepositoryTestConfiguration {

        @Bean
        PriceRepository priceRepository(final PriceReadRepository repository) {
            return new PriceRepositoryImpl(repository);
        }

        @Bean
        MailService mailService() {
            return Mockito.mock(MailService.class);
        }

        @Bean
        RouteLogServicePort routeLogServicePort() {
            return Mockito.mock(RouteLogServicePort.class);
        }

        @Bean
        SoftwareConfigurationServicePort softwareConfigurationServicePort() {
            return Mockito.mock(SoftwareConfigurationServicePort.class);
        }

        @Bean
        VoronoiService voronoiService() {
            return Mockito.mock(VoronoiService.class);
        }

        @Bean
        public ShipmentEventListener shipmentEventListener() {
            return Mockito.mock(ShipmentEventListener.class);
        }

        @Bean
        public ShipmentInternalController shipmentController() {
            return Mockito.mock(ShipmentInternalController.class);
        }

        @Bean
        public PathFinderServicePort pathFinderServicePort() {
            return Mockito.mock(PathFinderServicePort.class);
        }

        @Bean
        public SignatureRepository signatureRepository() {
            return Mockito.mock(SignatureRepository.class);
        }

        @Bean
        public ExternalFeignClient externalMicroserviceFeignClient() {
            return Mockito.mock(ExternalFeignClient.class);
        }

        @Bean
        public NotificationEventPublisher notificationEventPublisher() {
            return Mockito.mock(NotificationEventPublisher.class);
        }

        @Bean
        public ReturnProperties returnProperties() {
            final ReturnProperties returnProperties = new ReturnProperties();
            returnProperties.setUrl("http://localhost:8070");
            returnProperties.setEndpoint("/v2/api/returns");
            return returnProperties;
        }
    }

    @Autowired
    private ShipmentReadRepository repository;

    @Test
    void shouldFindById() {
        final ShipmentId shipmentId = new ShipmentId(100001L);
        final Optional<ShipmentEntity> parcel = repository.findById(shipmentId);
        assertTrue(parcel.isPresent());
    }

    @Test
    void shouldNotFindById() {
        final ShipmentId shipmentId = new ShipmentId(1L);
        final Optional<ShipmentEntity> parcel = repository.findById(shipmentId);
        assertFalse(parcel.isPresent());
    }
}
