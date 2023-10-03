package com.warehouse.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;
import com.warehouse.delivery.domain.model.Delivery;
import com.warehouse.delivery.domain.model.SupplierSignature;
import com.warehouse.delivery.domain.model.SupplierTokenRequest;
import com.warehouse.delivery.domain.model.SupplierTokenResponse;
import com.warehouse.delivery.domain.port.secondary.DeliveryRepository;
import com.warehouse.delivery.domain.port.secondary.SupplierTokenServicePort;
import com.warehouse.delivery.domain.service.DeliveryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceImplTest {


    @Mock
    private DeliveryRepository repository;

    @Mock
    private SupplierTokenServicePort supplierTokenServicePort;

    private DeliveryServiceImpl deliveryService;

    private final UUID DELIVERY_ID = UUID.fromString("2ea6ba6b-3f01-4d04-ad6d-952a186f48ac");

    private final UUID DELIVERY_ID_2 = UUID.fromString("2ea6ba6b-3f01-4d04-ad6d-952a186f48ab");


    @BeforeEach
    void setup() {
        deliveryService = new DeliveryServiceImpl(repository, supplierTokenServicePort);
    }

    @Test
    void shouldSave() {
        // given
        final List<Delivery> deliveries = createDeliveryList();
        final SupplierTokenRequest request = new SupplierTokenRequest("code", DELIVERY_ID);
        final SupplierSignature signature = SupplierSignature.builder()
                .token("token")
                .id(DELIVERY_ID)
                .supplierCode("code")
                .build();

        final Delivery delivery = createDelivery();
        delivery.setId(DELIVERY_ID);

        when(repository.saveDelivery(createDelivery())).thenReturn(delivery);

        doReturn(new SupplierTokenResponse(signature))
                .when(supplierTokenServicePort)
                .protect(request);
        // when
        final List<Delivery> deliveryWithToken = deliveryService.save(deliveries);
        // then
        final String token = deliveryWithToken.stream().map(Delivery::getToken).findAny().orElse(null);
        assertEquals(expectedToBe("token"), token);
    }

    @Test
    void shouldSaveAndSignDeliveryForOnlyOneRequest() {
        // given
        final Delivery delivery1 = createDelivery();
        final Delivery delivery2 = createDelivery();

        // set second delivery wrong supplier code that is not in STS
        delivery2.setSupplierCode("illegal_code");

        // build delivery requestl ist
        final List<Delivery> deliveries = Arrays.asList(delivery1, delivery2);

        // build mock requests for STS
        final SupplierTokenRequest request = new SupplierTokenRequest("code", DELIVERY_ID);
        final SupplierTokenRequest request2 = new SupplierTokenRequest("illegal_code", DELIVERY_ID_2);

        // correct signature from STS
        final SupplierSignature signature = SupplierSignature.builder()
                .token("token")
                .id(DELIVERY_ID)
                .supplierCode("code")
                .build();

        // wrong signature from STS, incomplete without token
        final SupplierSignature signature2 = SupplierSignature.builder()
                .id(DELIVERY_ID_2)
                .supplierCode("illegal_code")
                .build();

        signDeliveriesWithId(delivery1, delivery2);

        when(repository.saveDelivery(delivery1)).thenReturn(delivery1);
        when(repository.saveDelivery(delivery2)).thenReturn(delivery2);

        doReturn(new SupplierTokenResponse(signature))
                .when(supplierTokenServicePort)
                .protect(request);

        doReturn(new SupplierTokenResponse(signature2))
                .when(supplierTokenServicePort)
                .protect(request2);

        // when
        final List<Delivery> deliveryWithToken = deliveryService.save(deliveries);
        // then
        final String token = deliveryWithToken.stream()
                        .filter(delivery -> delivery.getId().equals(DELIVERY_ID))
                        .map(Delivery::getToken)
                        .findAny().orElse(null);
        assertEquals(expectedToBe("token"), token);

        // token from second signature that should not exist, should be equal to delivery2
        final Delivery notSignedDelivery = deliveryWithToken.stream()
                .filter(delivery -> delivery.getId().equals(DELIVERY_ID_2))
                .findAny()
                .orElse(null);
        assertEquals(expectedToBe(delivery2), notSignedDelivery);
    }

    private void signDeliveriesWithId(Delivery delivery1, Delivery delivery2) {
        delivery1.setId(DELIVERY_ID);
        delivery2.setId(DELIVERY_ID_2);
    }

    @Test
    void shouldNotSaveWhenSigningDeliveryIsNotPossible() {
        // given
        final List<Delivery> deliveries = createDeliveryList();
        final SupplierTokenRequest request = new SupplierTokenRequest("code", DELIVERY_ID);
        final SupplierSignature signature = SupplierSignature.builder()
                .id(DELIVERY_ID)
                .supplierCode("code")
                .build();

        final Delivery delivery = createDelivery();
        delivery.setId(DELIVERY_ID);

        when(repository.saveDelivery(createDelivery())).thenReturn(delivery);

        doReturn(new SupplierTokenResponse(signature))
                .when(supplierTokenServicePort)
                .protect(request);
        // when
        final List<Delivery> deliveryWithToken = deliveryService.save(deliveries);
        // then
        assertEquals(expectedToBe(null), deliveryWithToken.get(0).getToken());
    }

    private List<Delivery> createDeliveryList() {
        final Delivery delivery = Delivery.builder()
                .deliveryStatus(DeliveryStatus.DELIVERY)
                .parcelId(1L)
                .supplierCode("code")
                .depotCode("KT1")
                .build();
        return List.of(delivery);
    }

    private Delivery createDelivery() {
        return Delivery.builder()
                .deliveryStatus(DeliveryStatus.DELIVERY)
                .parcelId(1L)
                .supplierCode("code")
                .depotCode("KT1")
                .build();
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
