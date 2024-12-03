package com.warehouse.delivery;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.delivery.domain.vo.*;
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
import com.warehouse.delivery.infrastructure.adapter.secondary.DeliveryTokenAdapter;
import com.warehouse.deliverytoken.domain.vo.Parcel;
import com.warehouse.deliverytoken.domain.vo.ParcelId;
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

    private final static String TOKEN = "abcdefghjklk";
    
    @Test
    void shouldSignDeliveryDelivery() {
        // given
		final DeliveryTokenRequest request = DeliveryTokenRequest.builder().supplier(new Supplier(SUPPLIER_CODE))
				.deliveryPackageRequests(
						List.of(DeliveryPackageRequest
								.builder().delivery(DeliveryInformation.builder()
										.deliveryStatus(DeliveryStatus.DELIVERY).parcelId(1L).depotCode("abc").build())
								.build()))
				.build();
        final Parcel parcel = new Parcel(1L, null, ShipmentType.PARENT, "KT1");
        when(parcelServicePort.downloadParcel(new ParcelId(1L)))
                .thenReturn(parcel);
        // when
        final DeliveryTokenResponse response = deliveryTokenAdapter.protect(request);
        // then
        assertThat(response.getSupplierSignature())
                .hasSize(1)
                .filteredOn(delivery -> delivery.getToken().equals(TOKEN));
        assertThat(response.getSupplierCode()).isEqualTo(SUPPLIER_CODE);

    }
}
