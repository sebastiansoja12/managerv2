package com.warehouse.routetracker;


import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import com.warehouse.routetracker.configuration.RouteTrackerTestConfiguration;
import com.warehouse.routetracker.infrastructure.adapter.secondary.*;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.DepotEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.SupplierEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ParcelType;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status;


@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
@Disabled
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

    private ParcelEntity parcel;

    private SupplierEntity supplier;

    private DepotEntity depot;

    private UserEntity user;

    private static final Long PARENT_RELATED_ID = 100001L;

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
