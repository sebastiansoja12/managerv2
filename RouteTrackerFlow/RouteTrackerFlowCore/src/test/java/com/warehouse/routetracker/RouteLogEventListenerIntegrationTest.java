package com.warehouse.routetracker;


import static com.google.common.collect.MoreCollectors.onlyElement;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.ParcelEntity;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.routetracker.infrastructure.adapter.secondary.exception.ParcelNotFoundException;
import com.warehouse.routetracker.infrastructure.api.dto.DeliveryInformationDto;
import com.warehouse.routetracker.infrastructure.api.event.DeliveryLogEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.warehouse.routetracker.configuration.RouteTrackerTestConfiguration;
import com.warehouse.routetracker.infrastructure.adapter.primary.RouteLogEventListener;
import com.warehouse.routetracker.infrastructure.adapter.secondary.ParcelReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteEntity;
import com.warehouse.routetracker.infrastructure.api.dto.ShipmentRequestDto;
import com.warehouse.routetracker.infrastructure.api.event.ShipmentLogEvent;

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
    void shouldHandleShipment() {
        // given
        final ShipmentLogEvent shipmentLogEvent = ShipmentLogEvent.builder()
                .shipmentRequest(new ShipmentRequestDto(PARCEL_ID))
                .build();
        // when
        eventListener.handle(shipmentLogEvent);
        // then
        final List<RouteEntity> routeEntityList = repository.findByParcelId(PARCEL_ID);
        assertFalse(routeEntityList.isEmpty());
    }

    @Test
    void shouldHandleDelivery() {
        // given
        final DeliveryLogEvent deliveryLogEvent = DeliveryLogEvent.builder()
                .deliveryInformation(Collections.singletonList(
                        createDeliveryInformationDto("token")
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
                        createDeliveryInformationDto(null)
                ))
                .build();
        // when
        eventListener.handle(deliveryLogEvent);
        // then
        final List<RouteEntity> routeEntityList = repository.findByParcelId(PARCEL_ID);
        assertThat(routeEntityList).isEmpty();
    }


    @Test
    void shouldNotHandleWhenParcelToUpdateWasNotFound() {
        // given
        final String exceptionMessage = "Parcel to update with id '%s' was not found";
        final Long parcelId = 1L;
        final DeliveryLogEvent deliveryLogEvent = DeliveryLogEvent.builder()
                .deliveryInformation(Collections.singletonList(
                        new DeliveryInformationDto(parcelId, DEPOT_CODE, SUPPLIER_CODE,"DELIVERY", "token"))
                ).build();
        // when
        final Executable executable = () -> eventListener.handle(deliveryLogEvent);
        // then
        final ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, executable);
        assertEquals(expected(String.format(exceptionMessage, parcelId)), exception.getMessage());
    }

    private <T> T expected(T t) {
        return t;
    }

    private DeliveryInformationDto createDeliveryInformationDto(String token) {
        return new DeliveryInformationDto(PARCEL_ID, DEPOT_CODE, SUPPLIER_CODE,"DELIVERY", token);
    }
}
