package com.warehouse.deliveryreturn;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.DeliveryReturnReadRepository;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.DeliveryReturnRepositoryImpl;

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
                .shipmentId(new ShipmentId(1L))
                .departmentCode(new DepartmentCode("KT1"))
                .supplierCode(new SupplierCode("abc"))
                .build();
        // when
        final DeliveryReturn deliveryReturn = deliveryReturnRepository.saveDeliveryReturn(deliveryReturnDetails);
        // then
        verify(deliveryReturnReadRepository, times(1)).save(any());
    }
}
