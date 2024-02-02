package com.warehouse.routetracker;


import static com.google.common.collect.MoreCollectors.onlyElement;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

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
import com.warehouse.routetracker.infrastructure.adapter.primary.RouteLogEventListener;
import com.warehouse.routetracker.infrastructure.adapter.secondary.ParcelReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.routetracker.infrastructure.api.dto.DeliveryInformationDto;
import com.warehouse.routetracker.infrastructure.api.event.DeliveryLogEvent;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/db.xml")
public class RouteLogEventListenerIntegrationTest {

    @Autowired
    private RouteLogEventListener eventListener;

    @Autowired
    private RouteReadRepository repository;

    @Autowired
    private ParcelReadRepository parcelReadRepository;

    private final Long PARCEL_ID = 101L;

    private final String DEPOT_CODE = "TST";

    private final String SUPPLIER_CODE = "TS_TST";


    @Test
    void shouldHandleDelivery() {
        // given
        final DeliveryLogEvent deliveryLogEvent = DeliveryLogEvent.builder()
                .deliveryInformation(Collections.singletonList(
                        createDeliveryInformation("token")
                ))
                .build();
        // when
        eventListener.handle(deliveryLogEvent);
        // then
        final Status status = repository.findByParcelId(PARCEL_ID).stream()
                .map(RouteEntity::getParcel)
                .map(ParcelEntity::getStatus)
                .collect(onlyElement());

        assertEquals(Status.DELIVERY, status);
    }

    @Test
    void shouldHandleDeliveryButSkipDeliveryWhenTokenDoesNotExist() {
        // given
        final DeliveryLogEvent deliveryLogEvent = DeliveryLogEvent.builder()
                .deliveryInformation(Collections.singletonList(
                        createDeliveryInformation(null)
                ))
                .build();
        // when
        eventListener.handle(deliveryLogEvent);
        // then
        final List<RouteEntity> routeEntityList = repository.findByParcelId(PARCEL_ID);
        assertThat(routeEntityList).isEmpty();
    }

    private DeliveryInformationDto createDeliveryInformation(String token) {
        return DeliveryInformationDto.builder()
                .parcelId(PARCEL_ID)
                .depotCode(DEPOT_CODE)
                .supplierCode(SUPPLIER_CODE)
                .deliveryStatus("DELIVERY")
                .token(token)
                .build();
    }
}
