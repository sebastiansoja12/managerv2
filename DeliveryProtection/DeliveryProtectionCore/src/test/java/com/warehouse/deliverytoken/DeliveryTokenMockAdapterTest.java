package com.warehouse.deliverytoken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.warehouse.commonassets.enumeration.ParcelType;
import com.warehouse.deliverytoken.domain.vo.*;
import org.junit.jupiter.api.Test;

import com.warehouse.deliverytoken.domain.model.*;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.DeliveryTokenMockAdapter;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.exception.SupplierNotAllowedException;
import com.warehouse.deliverytoken.infrastructure.adapter.secondary.model.SupplierToken;

public class DeliveryTokenMockAdapterTest {

    private final Map<String, SupplierToken> supplierTokenMap = Map.of("dwvscq", new SupplierToken("tokenProtection"));
    private final DeliveryTokenMockAdapter deliveryTokenMockAdapter = new DeliveryTokenMockAdapter(supplierTokenMap);

    @Test
    void shouldProtectDelivery() {
        // given
        final DeliveryTokenRequest request = new DeliveryTokenRequest(
                createDeliveryRequests(1L, null, null, null, "1"),
                new Supplier("dwvscq")
        );
        // when
        final DeliveryTokenResponse response = deliveryTokenMockAdapter.protect(request);
        // then
        assertThat(response).isNotNull();
    }

    @Test
    void shouldNotProtectDeliveryWhenSupplierHasNoneAccessToDeliveries() {
        // given
        final DeliveryTokenRequest request = new DeliveryTokenRequest(
                createDeliveryRequests(1L, null, null, null, "1"),
                new Supplier("abc")
        );
        // when && then
        assertThrows(SupplierNotAllowedException.class, ()
                -> deliveryTokenMockAdapter.protect(request));
    }

	private List<DeliveryPackageRequest> createDeliveryRequests(Long parcelId, Long parcelRelatedId,
                                                                ParcelType parcelType, String destination, String deliveryId) {
		return Collections.singletonList(
				new DeliveryPackageRequest(createParcel(parcelId, parcelRelatedId, parcelType, destination),
						 createDelivery(deliveryId)));
	}

    private Delivery createDelivery(String id) {
        return new Delivery(id);
    }

    private Parcel createParcel(Long id, Long parcelRelatedId, ParcelType parcelType, String destination) {
        return new Parcel(id, parcelRelatedId, parcelType, destination);
    }
}
