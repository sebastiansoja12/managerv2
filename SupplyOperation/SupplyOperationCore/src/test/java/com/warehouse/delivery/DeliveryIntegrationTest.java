package com.warehouse.delivery;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

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
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.delivery.configuration.DeliveryTestConfiguration;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.DeliveryResponse;
import com.warehouse.delivery.domain.port.primary.DeliveryPort;
import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;
import com.warehouse.deliverytoken.domain.vo.Parcel;
import com.warehouse.deliverytoken.domain.vo.ParcelId;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.SupplierNotAllowedException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeliveryTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeliveryIntegrationTest {

    @Autowired
    private DeliveryPort deliveryPort;

    @Autowired
    private com.warehouse.deliverytoken.domain.port.secondary.DeliveryTokenServicePort deliveryTokenServicePort;

    @Autowired
    private ParcelServicePort parcelServicePort;

    @Test
    void shouldDeliver() {
        // given
        final Set<DeliveryRequest> deliveryRequestList = Collections.singleton(createDeliveryRequest());
        final Parcel parcel = new Parcel(1L, null, ShipmentType.PARENT, "KT1");
        when(parcelServicePort.downloadParcel(new ParcelId(1L)))
                .thenReturn(parcel);
        // when
        final Set<DeliveryResponse> deliveryResponses = deliveryPort.processDelivery(deliveryRequestList);
        // then

    }

    @Test
    void shouldNotDeliverWhenSupplierCodeDoesNotMatchOneFromMock() {
        // given
        final Set<DeliveryRequest> deliveryRequestList = Collections.singleton(DeliveryRequest.builder()
                .build());
        final Parcel parcel = new Parcel(1L, null, ShipmentType.PARENT, "KT1");
        when(parcelServicePort.downloadParcel(new ParcelId(1L)))
                .thenReturn(parcel);
        // when && then
        assertThrows(SupplierNotAllowedException.class, () -> deliveryPort.processDelivery(deliveryRequestList));
    }

    private DeliveryRequest createDeliveryRequest() {
        return DeliveryRequest.builder()
                .build();
    }

}
