package com.warehouse.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
import com.warehouse.delivery.configuration.DeliveryTestConfiguration;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.port.primary.DeliveryPort;
import com.warehouse.deliverytoken.domain.enumeration.ParcelType;
import com.warehouse.deliverytoken.domain.model.Parcel;
import com.warehouse.deliverytoken.domain.model.ParcelId;
import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.SupplierNotAllowedException;
import com.warehouse.routetracker.infrastructure.api.RouteLogEventPublisher;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeliveryTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeliveryIntegrationTest {

    @Autowired
    private DeliveryPort deliveryPort;

    @Autowired
    private RouteLogEventPublisher routeLogEventPublisher;

    @Autowired
    private com.warehouse.deliverytoken.domain.port.secondary.DeliveryTokenServicePort deliveryTokenServicePort;

    @Autowired
    private ParcelServicePort parcelServicePort;

    @Test
    void shouldDeliver() {
        // given
        final List<DeliveryRequest> deliveryRequestList = Collections.singletonList(createDeliveryRequest());
        final Parcel parcel = new Parcel(1L, null, ParcelType.PARENT, "KT1");
        when(parcelServicePort.downloadParcel(new ParcelId(1L)))
                .thenReturn(parcel);
        // when
        final List<DeliveryResponse> deliveryResponses = deliveryPort.deliver(deliveryRequestList);
        // then
        assertThat(deliveryResponses)
                .extracting(
                        DeliveryResponse::getId,
                        DeliveryResponse::getParcelId,
                        DeliveryResponse::getDeliveryStatus
				).hasOnlyOneElementSatisfying(
						tuple -> assertThat(tuple)
                                .extracting(
                                        id -> 1L,
                                        parcelId -> 1L,
                                        deliveryStatus -> "DELIVERY")
                );
    }

    @Test
    void shouldNotDeliverWhenSupplierCodeDoesNotMatchOneFromMock() {
        // given
        final List<DeliveryRequest> deliveryRequestList = Collections.singletonList(DeliveryRequest.builder()
                .depotCode("KT1")
                .supplierCode("abc")
                .parcelId(1L)
                .build());
        final Parcel parcel = new Parcel(1L, null, ParcelType.PARENT, "KT1");
        when(parcelServicePort.downloadParcel(new ParcelId(1L)))
                .thenReturn(parcel);
        // when && then
        assertThrows(SupplierNotAllowedException.class, () -> deliveryPort.deliver(deliveryRequestList));
    }

    private DeliveryRequest createDeliveryRequest() {
        return DeliveryRequest.builder()
                .depotCode("KT1")
                .supplierCode("dwvscq")
                .parcelId(1L)
                .build();
    }

}
