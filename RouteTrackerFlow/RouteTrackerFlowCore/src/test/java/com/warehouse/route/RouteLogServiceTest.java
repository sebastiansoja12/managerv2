package com.warehouse.route;

import com.warehouse.route.configuration.RouteTrackerTestConfiguration;
import com.warehouse.route.domain.model.Route;
import com.warehouse.route.domain.model.RouteResponse;
import com.warehouse.route.domain.model.Routes;
import com.warehouse.route.domain.port.secondary.RouteLogService;
import com.warehouse.route.domain.port.secondary.RouteRepository;
import com.warehouse.route.infrastructure.adapter.secondary.ParcelReadRepository;
import com.warehouse.route.infrastructure.adapter.secondary.RouteDepotReadRepository;
import com.warehouse.route.infrastructure.adapter.secondary.RouteSupplierReadRepository;
import com.warehouse.route.infrastructure.adapter.secondary.UserReadRepository;
import com.warehouse.route.infrastructure.adapter.secondary.entity.DepotEntity;
import com.warehouse.route.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.route.infrastructure.adapter.secondary.entity.SupplierEntity;
import com.warehouse.route.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.route.infrastructure.adapter.secondary.enumeration.ParcelType;
import com.warehouse.route.infrastructure.adapter.secondary.enumeration.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
public class RouteLogServiceTest {


    @Autowired
    private RouteLogService routeLogService;

    @Autowired
    private ParcelReadRepository parcelReadRepository;

    @Autowired
    private RouteRepository repository;

    @Autowired
    private RouteSupplierReadRepository routeSupplierReadRepository;

    @Autowired
    private RouteDepotReadRepository depotReadRepository;

    @Autowired
    private UserReadRepository userReadRepository;

    private ParcelEntity parcel;

    private SupplierEntity supplier;

    private DepotEntity depot;

    private UserEntity user;

    @BeforeEach
    void setup() {
        // saving parcel in database so we can find it during saving its route
        parcel = createParcelEntity();
        parcelReadRepository.save(parcel);
        // saving supplier in database so we can find it during saving parcels route
        supplier = createSupplierEntity();
        routeSupplierReadRepository.save(supplier);
        // saving depot in database so we can find it during saving parcels route
        depot = createDepotEntity();
        depotReadRepository.save(depot);
        // saving user in database so we can find it during saving parcels route
        user = createUserEntity();
        userReadRepository.save(user);

    }

    @Test
    @Disabled
    void shouldInitializeRoute() {
        // given
        final Route route = Route.builder()
                .parcelId(parcel.getId())
                .created(LocalDateTime.now())
                .build();
        // when
        routeLogService.initializeRoute(route);
        // then
        final List<Routes> routes = repository.findByParcelId(parcel.getId());
        assertThat(routes.size()).isGreaterThan(0);
    }

    @Test
    void shouldSaveSupplyRoute() {
        // given
        final Route route = Route.builder()
                .parcelId(parcel.getId())
                .created(LocalDateTime.now())
                .build();
        // when
        final RouteResponse response = routeLogService.saveSupplyRoute(route);
        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isInstanceOf(UUID.class);
    }

    @Test
    void shouldSaveRoute() {
        // given
        final Route route = Route.builder()
                .parcelId(parcel.getId())
                .created(LocalDateTime.now())
                .build();
        // when
        final RouteResponse response = routeLogService.saveRoute(route);
        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isInstanceOf(UUID.class);
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
                .status(com.warehouse.route.infrastructure.adapter.secondary.enumeration.Status.CREATED)
                .parcelType(ParcelType.PARENT)
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
