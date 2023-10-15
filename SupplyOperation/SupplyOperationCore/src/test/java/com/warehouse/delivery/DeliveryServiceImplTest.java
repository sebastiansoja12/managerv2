package com.warehouse.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

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
        final List<Delivery> deliveries = createDeliveryList();
        final DeliveryPackageRequest deliveryPackageRequest = createDeliveryPackageRequest(
                1L, new Supplier("code"), Delivery.builder()
                        .id(DELIVERY_ID)
                        .deliveryStatus(DeliveryStatus.DELIVERY)
                        .parcelId(1L)
                        .depotCode("depotCode")
                        .supplierCode("code")
                        .build()
        );
        final DeliveryTokenRequest request = new DeliveryTokenRequest(List.of(deliveryPackageRequest));
        final SupplierSignature signature = SupplierSignature.builder()
                .token("token")
                .deliveryId(DELIVERY_ID)
                .supplierCode("code")
                .build();

        final Delivery delivery = createDelivery();
        delivery.setId(DELIVERY_ID);

        when(repository.saveDelivery(createDelivery())).thenReturn(delivery);

        doReturn(new DeliveryTokenResponse(List.of(signature)))
                .when(supplierTokenServicePort)
                .protect(request);
        // when
        final List<Delivery> deliveryWithToken = deliveryService.save(deliveries);
        // then
        final String token = deliveryWithToken.stream().map(Delivery::getToken).findAny().orElse(null);
        assertEquals(expectedToBe("token"), token);
    }

    @Test
    void shouldNotSaveWhenSigningDeliveryIsNotPossible() {
        // given
        final List<Delivery> deliveries = createDeliveryList();
        final DeliveryTokenRequest request = new DeliveryTokenRequest(
                List.of(createDeliveryPackageRequest(
                        1L, new Supplier("code"), Delivery.builder()
                                .id(DELIVERY_ID)
                                .deliveryStatus(DeliveryStatus.DELIVERY)
                                .parcelId(1L)
                                .depotCode("depotCode")
                                .supplierCode("code")
                                .build())));
        final SupplierSignature signature = SupplierSignature.builder()
                .deliveryId(DELIVERY_ID)
                .supplierCode("code")
                .build();

        final Delivery delivery = createDelivery();
        delivery.setId(DELIVERY_ID);

        when(repository.saveDelivery(createDelivery())).thenReturn(delivery);

        doReturn(new DeliveryTokenResponse(List.of(signature)))
                .when(supplierTokenServicePort)
                .protect(request);
        // when
        final List<Delivery> deliveryWithToken = deliveryService.save(deliveries);
        // then
        assertEquals(expectedToBe(null), deliveryWithToken.get(0).getToken());
    }

    private DeliveryPackageRequest createDeliveryPackageRequest(Long id, Supplier supplier, Delivery delivery) {
        return new DeliveryPackageRequest(id, supplier, delivery);
    }

    private List<Delivery> createDeliveryList() {
        final Delivery delivery = Delivery.builder()
                .deliveryStatus(DeliveryStatus.DELIVERY)
                .parcelId(1L)
                .supplierCode("code")
                .depotCode("depotCode")
                .build();
        return List.of(delivery);
    }

    private Delivery createDelivery() {
        return Delivery.builder()
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
