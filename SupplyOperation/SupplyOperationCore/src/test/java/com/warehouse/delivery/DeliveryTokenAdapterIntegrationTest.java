package com.warehouse.delivery;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
import com.warehouse.delivery.domain.enumeration.DeliveryStatus;
import com.warehouse.delivery.domain.model.*;
import com.warehouse.delivery.infrastructure.adapter.secondary.DeliveryTokenAdapter;
import com.warehouse.deliverytoken.domain.enumeration.ParcelType;
import com.warehouse.deliverytoken.domain.model.Parcel;
import com.warehouse.deliverytoken.domain.model.ParcelId;
import com.warehouse.deliverytoken.domain.port.secondary.ParcelServicePort;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeliveryTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeliveryTokenAdapterIntegrationTest {
    
    
    @Autowired
    private DeliveryTokenAdapter deliveryTokenAdapter;

    @Autowired
    private ParcelServicePort parcelServicePort;

    private final static String SUPPLIER_CODE = "dwvscq";
    
    @Test
    void shouldProtectDelivery() {
        // given
        final DeliveryTokenRequest request = new DeliveryTokenRequest( 
             List.of(new DeliveryPackageRequest(DeliveryInformation.builder()
                     .deliveryStatus(DeliveryStatus.DELIVERY)
                     .parcelId(1L)
                     .depotCode("abc")
                     .build())), new Supplier(SUPPLIER_CODE));
        final Parcel parcel = new Parcel(1L, null, ParcelType.PARENT, "KT1");
        when(parcelServicePort.downloadParcel(new ParcelId(1L)))
                .thenReturn(parcel);
        // when
        final DeliveryTokenResponse response = deliveryTokenAdapter.protect(request);
        // then
        assertThat(response.getSupplierSignature())
                .hasSize(1)
                .filteredOn(delivery -> delivery.getToken().equals(SUPPLIER_CODE));
        assertThat(response.getSupplierCode()).isEqualTo(SUPPLIER_CODE);

    }
}
