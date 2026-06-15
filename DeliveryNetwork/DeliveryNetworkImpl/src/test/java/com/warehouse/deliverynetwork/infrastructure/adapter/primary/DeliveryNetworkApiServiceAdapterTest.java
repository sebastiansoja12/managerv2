package com.warehouse.deliverynetwork.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.api.dto.DepartmentIdDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryPathDto;
import com.warehouse.deliverynetwork.application.port.primary.DeliveryNetworkPort;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper.DeliveryNetworkApiMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryNetworkApiServiceAdapterTest {

    @Mock
    private DeliveryNetworkPort deliveryNetworkPort;

    private DeliveryNetworkApiServiceAdapter deliveryNetworkApiServiceAdapter;

    @BeforeEach
    void setUp() {
        this.deliveryNetworkApiServiceAdapter = new DeliveryNetworkApiServiceAdapter(
                this.deliveryNetworkPort, new DeliveryNetworkApiMapper());
    }

    @Test
    void shouldCheckDirectConnectionUsingTypedDepartmentIds() {
        final DepartmentId firstDepartmentId = new DepartmentId(1L);
        final DepartmentId secondDepartmentId = new DepartmentId(2L);
        when(this.deliveryNetworkPort.areDirectlyConnected(firstDepartmentId, secondDepartmentId)).thenReturn(true);

        final boolean directlyConnected = this.deliveryNetworkApiServiceAdapter.areDirectlyConnected(
                new DepartmentIdDto(1L), new DepartmentIdDto(2L));

        assertTrue(directlyConnected);
        verify(this.deliveryNetworkPort).areDirectlyConnected(firstDepartmentId, secondDepartmentId);
    }

    @Test
    void shouldReturnTypedDeliveryPath() {
        final DepartmentId sourceDepartmentId = new DepartmentId(1L);
        final DepartmentId targetDepartmentId = new DepartmentId(3L);
        when(this.deliveryNetworkPort.findDeliveryPath(sourceDepartmentId, targetDepartmentId))
                .thenReturn(new DeliveryPath(List.of(
                        sourceDepartmentId,
                        new DepartmentId(2L),
                        targetDepartmentId)));

        final DeliveryPathDto deliveryPath = this.deliveryNetworkApiServiceAdapter.findDeliveryPath(
                new DepartmentIdDto(1L), new DepartmentIdDto(3L));

        assertEquals(
                List.of(new DepartmentIdDto(1L), new DepartmentIdDto(2L), new DepartmentIdDto(3L)),
                deliveryPath.departmentIds());
    }
}
