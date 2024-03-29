package com.warehouse.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.warehouse.delivery.domain.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;
import com.warehouse.delivery.domain.model.*;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.delivery.domain.service.DeliveryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceImplTest {


    @Mock
    private DeliveryRepository repository;

    @Mock
    private DeliveryTokenServicePort supplierTokenServicePort;

    private DeliveryServiceImpl deliveryService;

    private final UUID DELIVERY_ID = UUID.fromString("2ea6ba6b-3f01-4d04-ad6d-952a186f48ac");


    @BeforeEach
    void setup() {
        deliveryService = new DeliveryServiceImpl(repository, supplierTokenServicePort);
    }

    @Test
    void shouldSave() {
        // given
        final DeliveryPackageRequest deliveryPackageRequest = createDeliveryPackageRequest(
                 DeliveryInformation.builder()
                        .id(DELIVERY_ID)
                        .deliveryStatus(DeliveryStatus.DELIVERY)
                        .parcelId(1L)
                        .depotCode("depotCode")
                        .build()
        );
        final DeliveryTokenRequest request = DeliveryTokenRequest.builder()
                .deliveryPackageRequests(List.of(deliveryPackageRequest))
                .supplier(new Supplier("code"))
                .build();
        final SupplierSignature signature = SupplierSignature.builder()
                .token("token")
                .deliveryId(DELIVERY_ID)
                .build();

        final DeliveryRequest delivery = createDeliveryRequest();

        when(repository.saveDelivery(createDeliveryRequest())).thenReturn(createDelivery());

        doReturn(DeliveryTokenResponse.builder()
                .supplierSignature(List.of(signature))
                .supplierCode("code")
                .build())
                .when(supplierTokenServicePort)
                .protect(request);
        // when
        final List<Delivery> deliveryWithToken = deliveryService.save(Set.of(delivery));
        // then
        final String token = deliveryWithToken.stream().map(Delivery::getToken).findAny().orElse(null);
        assertEquals(expectedToBe("token"), token);
    }

    @Test
    void shouldNotSaveWhenSigningDeliveryIsNotPossible() {
        // given
        final DeliveryTokenRequest request = DeliveryTokenRequest.builder()
                .supplier(new Supplier("code"))
                .deliveryPackageRequests(List.of(createDeliveryPackageRequest(
                        DeliveryInformation.builder()
                                .id(DELIVERY_ID)
                                .deliveryStatus(DeliveryStatus.DELIVERY)
                                .parcelId(1L)
                                .depotCode("depotCode")
                                .build())))
                .build();
        final SupplierSignature signature = SupplierSignature.builder()
                .deliveryId(DELIVERY_ID)
                .build();

        final DeliveryRequest delivery = createDeliveryRequest();

        when(repository.saveDelivery(createDeliveryRequest())).thenReturn(createDelivery());

        doReturn(DeliveryTokenResponse.builder()
                .supplierSignature(List.of(signature))
                .supplierCode("code")
                .build())
                .when(supplierTokenServicePort)
                .protect(request);
        // when
        final List<Delivery> deliveryWithToken = deliveryService.save(Set.of(delivery));
        // then
        assertEquals(expectedToBe(null), deliveryWithToken.get(0).getToken());
    }

	private DeliveryPackageRequest createDeliveryPackageRequest(DeliveryInformation delivery) {
        return DeliveryPackageRequest.builder()
                .delivery(delivery)
                .build();
    }

    private DeliveryRequest createDeliveryRequest() {
        return DeliveryRequest.builder()
                .deliveryStatus(DeliveryStatus.DELIVERY)
                .parcelId(1L)
                .supplierCode("code")
                .depotCode("depotCode")
                .build();
    }

    private Delivery createDelivery() {
        return Delivery.builder()
                .id(DELIVERY_ID)
                .deliveryStatus(DeliveryStatus.DELIVERY)
                .parcelId(1L)
                .supplierCode("code")
                .depotCode("depotCode")
                .build();
    }

    private <T> T expectedToBe(T t) {
        return t;
    }

}
