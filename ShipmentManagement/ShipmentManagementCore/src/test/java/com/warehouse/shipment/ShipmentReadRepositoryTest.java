package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.mail.domain.service.MailService;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentReadRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
import com.warehouse.voronoi.VoronoiService;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = ShipmentReadRepositoryTest.ShipmentReadRepositoryTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/shipment.xml")
public class ShipmentReadRepositoryTest {

    @ComponentScan(basePackages = { "com.warehouse.shipment"})
    @EntityScan(basePackages = { "com.warehouse.shipment"})
    @EnableJpaRepositories(basePackages = { "com.warehouse.shipment"})
    public static class ShipmentReadRepositoryTestConfiguration {

        @MockBean
        public MailService mailService;

        @MockBean
        public ShipmentRepository shipmentRepository;

        @MockBean
        public RouteLogServicePort routeLogServicePort;

        @MockBean
        public SoftwareConfigurationServicePort softwareConfigurationServicePort;

        @MockBean
        public VoronoiService voronoiService;
    }

    @Autowired
    private ShipmentReadRepository repository;

    @Test
    void shouldFindById() {
        // given
        final ShipmentId shipmentId = new ShipmentId(100001L);
        // when
        final Optional<ShipmentEntity> parcel = repository.findById(shipmentId);
        // then
        assertTrue(parcel.isPresent());
    }

    @Test
    void shouldNotFindById() {
        // given
        final ShipmentId shipmentId = new ShipmentId(1L);
        // when
        final Optional<ShipmentEntity> parcel = repository.findById(shipmentId);
        // then
        assertFalse(parcel.isPresent());
    }
}
