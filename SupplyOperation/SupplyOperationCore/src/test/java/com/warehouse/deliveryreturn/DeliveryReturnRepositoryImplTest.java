package com.warehouse.deliveryreturn;

import com.warehouse.deliveryreturn.domain.enumeration.DeliveryStatus;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.DeliveryReturnReadRepository;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.DeliveryReturnRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeliveryReturnRepositoryImplTest {

    @Mock
    private DeliveryReturnReadRepository deliveryReturnReadRepository;

    private DeliveryReturnRepositoryImpl deliveryReturnRepository;

    @BeforeEach
    void setup() {
        deliveryReturnRepository = new DeliveryReturnRepositoryImpl(deliveryReturnReadRepository);
    }

    @Test
    void shouldSaveDeliveryReturn() {
        // given
        final DeliveryReturnDetails deliveryReturnDetails = DeliveryReturnDetails.builder()
                .deliveryStatus(DeliveryStatus.RETURN)
                .token("12345")
                .parcelId(1L)
                .depotCode("KT1")
                .supplierCode("abc")
                .build();
        // when
        final DeliveryReturn deliveryReturn = deliveryReturnRepository.saveDeliveryReturn(deliveryReturnDetails);
        // then
        verify(deliveryReturnReadRepository, times(1)).save(any());
    }
}
