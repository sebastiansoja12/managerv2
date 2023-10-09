package com.warehouse.routetracker;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.warehouse.routetracker.configuration.RouteTrackerTestConfiguration;
import com.warehouse.routetracker.infrastructure.adapter.secondary.ParcelReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ParcelType;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/db.xml")
public class TrackerIntegrationTest {

    @Autowired
    private RouteTrackerLogPort routeTrackerLogPort;

    @Autowired
    private ParcelReadRepository parcelReadRepository;

    @Autowired
    private RouteReadRepository routeReadRepository;

    private static final Long PARENT_RELATED_ID = 100001L;

    @Test
    void shouldInitializeRoute() {
        // given
        final Long parcelId = 100L;
        // when
        routeTrackerLogPort.initializeRoute(parcelId);
        // then
        final List<RouteEntity> routeEntityList = routeReadRepository.findByParcelId(parcelId);
        assertFalse(routeEntityList.isEmpty());
    }

    @Test
    void shouldSaveDelivery() {
        // given

        // when

        // then
    }

    @Test
    void shouldDeleteRoute() {
        // given

        // when

        // then
    }

    @Test
    void shouldSaveRoutes() {
        // given

        // when

        // then
    }

    private ParcelEntity createParcelEntity() {
        return ParcelEntity.builder()
                .parcelSize(Size.TEST)
                .firstName("test")
                .lastName("test")
                .senderTelephone("123")
                .senderCity("test")
                .senderEmail("test@wp.pl")
                .senderStreet("test")
                .senderPostalCode("00-000")
                .recipientFirstName("test")
                .recipientLastName("test")
                .recipientCity("test")
                .recipientEmail("test@wp.pl")
                .recipientStreet("test")
                .recipientPostalCode("00-000")
                .recipientTelephone("1233")
                .destination("KT1")
                .status(Status.REDIRECT)
                .parcelType(ParcelType.CHILD)
                .parcelRelatedId(PARENT_RELATED_ID)
                .build();
    }

    private DepotEntity createDepotEntity() {
        return DepotEntity.builder()
                .depotCode("TST")
                .city("Test")
                .country("Test")
                .street("test")
                .build();
    }

    private SupplierEntity createSupplierEntity() {
        return SupplierEntity.builder()
                .supplierCode("TS_TST")
                .depot(null)
                .firstName("test")
                .lastName("test")
                .telephone("123")
                .build();
    }

    private UserEntity createUserEntity() {
        return UserEntity.builder()
                .username("test")
                .password("[password]")
                .depotCode("TST")
                .email("test@wp.pl")
                .lastName("test")
                .firstName("test")
                .role("admin")
                .build();
    }



}
