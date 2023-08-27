package com.warehouse.route;


import com.warehouse.route.configuration.RouteTrackerTestConfiguration;
import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.port.secondary.RouteLogService;
import com.warehouse.route.infrastructure.adapter.secondary.*;
import com.warehouse.route.infrastructure.adapter.secondary.entity.DepotEntity;
import com.warehouse.route.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.route.infrastructure.adapter.secondary.entity.SupplierEntity;
import com.warehouse.route.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.route.infrastructure.adapter.secondary.enumeration.ParcelType;
import com.warehouse.route.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.route.infrastructure.adapter.secondary.enumeration.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * This class tests entire process of a package lifetime
 * Creating shipment -> initializing route
 * Supplier takes the package and registers it in database -> SupplySaveRoute
 * Parcel is delivered to depot where workers scan its QR code -> saveRoute
 * /Parcel is delivered between depots/
 * Another route for given parcel is saved - saveRoute
 * Process for saving routes by depot workers can be implied all the time, till the package goes to the deliverer
 * Supplier delivers the package -> SupplySaveRoute
 * Parcel can be edited in any way everytime -> Event from WarehouseWeb module
 */
@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
public class TrackerIntegrationTest {

    @Autowired
    private ParcelReadRepository parcelReadRepository;

    @Autowired
    private RouteReadRepository routeReadRepository;

    @Autowired
    private RouteSupplierReadRepository routeSupplierReadRepository;

    @Autowired
    private RouteDepotReadRepository depotReadRepository;

    @Autowired
    private UserReadRepository userReadRepository;

    @Autowired
    private RouteLogService routeLogService;

    private ParcelEntity parcel;

    private SupplierEntity supplier;

    private DepotEntity depot;

    private UserEntity user;

    private static final Long PARENT_RELATED_ID = 100001L;


    @BeforeEach
    public void setup() {
        // saving parcel in database, so we can find it during saving its route
        parcel = createParcelEntity();
        parcelReadRepository.save(parcel);
        // saving supplier in database, so we can find it during saving parcels route
        supplier = createSupplierEntity();
        routeSupplierReadRepository.save(supplier);
        // saving depot in database, so we can find it during saving parcels route
        depot = createDepotEntity();
        depotReadRepository.save(depot);
        // saving user in database, so we can find it during saving parcels route
        user = createUserEntity();
        userReadRepository.save(user);

    }

    @Test
    @Disabled
    void shouldInitializeRoute() {
        // given
        final Route initializeRoute = Route.builder()
                .parcelId(parcel.getId())
                .created(LocalDateTime.now())
                .build();
        // when
        routeLogService.initializeRoute(initializeRoute);

        // then
        assertThat(routeReadRepository.findByParcelId(parcel.getId())).isNotNull();
    }

    @Test
    void shouldSaveSupplyRoute() {
        // given
        final Route supplyRoute = Route.builder()
                .parcelId(parcel.getId())
                //.supplierId(supplier.getId())
                .created(LocalDateTime.now())
                .build();
        // when
        final RouteResponse response = routeLogService.saveSupplyRoute(supplyRoute);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void shouldSaveRoute() {
        // given
        final Route route = Route.builder()
                .parcelId(parcel.getId())
                .userId((long) user.getId())
                .depotId(depot.getId())
                //.supplierId(supplier.getId())
                .created(LocalDateTime.now())
                .build();
        // when
        final RouteResponse response = routeLogService.saveRoute(route);
        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isInstanceOf(UUID.class);
    }

    @Test
    void shouldProcessThroughParcelLifetime() {
        // given
        final Route initializeRoute = Route.builder()
                .parcelId(parcel.getId())
                .created(LocalDateTime.now())
                .build();

        final Route supplyRoute = Route.builder()
                .parcelId(parcel.getId())
                //.supplierId(supplier.getId())
                .created(LocalDateTime.now())
                .build();

        final Route route = Route.builder()
                .parcelId(parcel.getId())
                //.supplierId(supplier.getId())
                .depotId(depot.getId())
                .userId((long) user.getId())
                .created(LocalDateTime.now())
                .build();
        // when
        routeLogService.initializeRoute(initializeRoute);

        // and: contractor registers the parcel
        final RouteResponse supplyResponse = routeLogService.saveSupplyRoute(supplyRoute);

        // and: warehouse worker registers the parcel in depot
        final RouteResponse routeResponse = routeLogService.saveRoute(route);
        // then
        assertAll(
                () -> assertThat(supplyResponse).isNotNull(),
                () -> assertThat(routeResponse).isNotNull(),
                () -> assertThat(supplyResponse.getId()).isInstanceOf(UUID.class),
                () -> assertThat(routeResponse.getId()).isInstanceOf(UUID.class)
        );
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
