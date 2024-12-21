package com.warehouse.deliveryreturn;

import com.warehouse.deliveryreturn.domain.vo.DeliveryPackageRequest;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnTokenResponse;
import com.warehouse.deliveryreturn.domain.vo.Supplier;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnInformation;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnSignature;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.DeliveryReturnServiceMockAdapter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeliveryReturnServiceMockAdapterTest {

    private final DeliveryReturnServiceMockAdapter adapter = new DeliveryReturnServiceMockAdapter();

    @Test
    void shouldSignDeliveryReturn() {
        // given
        final List<DeliveryPackageRequest> requests = List.of(DeliveryPackageRequest.builder()
                .delivery(DeliveryReturnInformation.builder()
                        .deliveryStatus("DELIVERY")
                        .departmentCode("KT1")
                        .shipmentId(1L)
                        .build())
                .build());
        final DeliveryReturnTokenRequest request = new DeliveryReturnTokenRequest(requests, new Supplier("abc",
                Boolean.TRUE));
        // when
        final DeliveryReturnTokenResponse response = adapter.sign(request);
        // then
        assertThat(response.getDeliveryReturnSignatures())
                .extracting(DeliveryReturnSignature::getToken)
                .containsExactly("12345");
    }
}
