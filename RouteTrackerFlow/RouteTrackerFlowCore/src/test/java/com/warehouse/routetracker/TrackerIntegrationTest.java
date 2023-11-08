package com.warehouse.routetracker;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;
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
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.routetracker.configuration.RouteTrackerTestConfiguration;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.domain.vo.DeliveryInformation;
import com.warehouse.routetracker.domain.vo.RouteDeleteRequest;
import com.warehouse.routetracker.domain.vo.RouteRequest;
import com.warehouse.routetracker.domain.vo.RouteResponse;
import com.warehouse.routetracker.infrastructure.adapter.secondary.ParcelReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.*;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ParcelType;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status;

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

    private static final Long PARCEL_ID = 100L;

    @Test
    void shouldInitializeRoute() {
        // when
        routeTrackerLogPort.initializeRoute(PARCEL_ID);
        // then
        final List<RouteEntity> routeEntityList = routeReadRepository.findByParcelId(PARCEL_ID);
        assertFalse(routeEntityList.isEmpty());
    }

    @Test
    void shouldSaveDelivery() {
        // given
        final List<DeliveryInformation> deliveryInformations = Collections.singletonList(
                DeliveryInformation.builder()
                        .deliveryStatus(com.warehouse.routetracker.domain.enumeration.Status.DELIVERY)
                        .depotCode("TST")
                        .parcelId(PARCEL_ID)
                        .supplierCode("TS_TST")
                        .token("token")
                        .build()
        );
        // when
        routeTrackerLogPort.saveDelivery(deliveryInformations);
        // then
        final List<RouteEntity> routeEntityList = routeReadRepository.findByParcelId(PARCEL_ID);
        assertFalse(routeEntityList.isEmpty());
    }

    @Test
    void shouldNotSaveDeliveryWhenTokenIsNotInRequest() {
        // given
        final List<DeliveryInformation> deliveryInformations = Collections.singletonList(
                DeliveryInformation.builder()
                        .deliveryStatus(com.warehouse.routetracker.domain.enumeration.Status.DELIVERY)
                        .depotCode("TST")
                        .parcelId(PARCEL_ID)
                        .supplierCode("TS_TST")
                        .build()
        );
        // when
        routeTrackerLogPort.saveDelivery(deliveryInformations);
        // then
        final List<RouteEntity> routeEntityList = routeReadRepository.findByParcelId(PARCEL_ID);
        assertTrue(routeEntityList.isEmpty());
    }

    @Test
    void shouldDeleteRoute() {
        // given
        final String id = "d8d53e7d-9175-4b5b-bf0d-bc209549c3a9";
        final RouteDeleteRequest request = RouteDeleteRequest.builder()
                .id(id)
                .build();
        // when
        routeTrackerLogPort.deleteRoute(request);
        // then
        final Optional<RouteEntity> routeEntity = routeReadRepository.findById(id);
        assertTrue(routeEntity.isEmpty());
    }

    @Test
    void shouldSaveRoutes() {
        // given
        final RouteRequest request = RouteRequest.builder()
                .parcelId(PARCEL_ID)
                .build();
        final List<RouteRequest> requests = Collections.singletonList(request);
        // when
        final List<RouteResponse> responses = routeTrackerLogPort.saveRoutes(requests);

        // then
        assertThat(responses).isNotEmpty();
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
                .role(Role.ADMIN)
                .build();
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
