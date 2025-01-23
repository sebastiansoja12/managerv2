package com.warehouse.deliveryreturn;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreturn.domain.model.ReturnTokenRequest;
import com.warehouse.deliveryreturn.domain.vo.*;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.ReturnTokenServiceMockAdapter;

public class ReturnTokenServiceMockAdapterTest {

    private final ReturnTokenServiceMockAdapter adapter = new ReturnTokenServiceMockAdapter();

    @Test
    void shouldSignDeliveryReturn() {
        // given
        final List<ReturnPackageRequest> requests = List.of(ReturnPackageRequest.builder().build());
        final ReturnTokenRequest request = new ReturnTokenRequest(requests, new Supplier(new SupplierCode("abc")));
        // when
        final ReturnTokenResponse response = adapter.sign(request);
        // then
        assertThat(response.getDeliveryReturnSignatures())
                .extracting(ReturnPackageResponse::getReturnToken)
                .extracting(ReturnToken::value)
                .containsExactly("12345");
    }
}
