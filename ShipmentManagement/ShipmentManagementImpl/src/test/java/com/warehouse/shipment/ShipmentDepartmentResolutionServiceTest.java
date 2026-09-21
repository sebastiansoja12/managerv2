package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.secondary.PickupPointServicePort;
import com.warehouse.shipment.application.service.ShipmentDepartmentResolutionService;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.enumeration.ShipmentSource;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ShipmentDepartmentResolutionServiceTest {

    private final PickupPointServicePort pickupPointServicePort = mock(PickupPointServicePort.class);

    private final ShipmentCreateCommand command = mock(ShipmentCreateCommand.class);

    private final ShipmentDepartmentResolutionService service = new ShipmentDepartmentResolutionService(
            pickupPointServicePort, mock(OperatorContextProvider.class));

    @Test
    void shouldLeaveTargetDepartmentOfCourierShipmentToDelivery() {
        when(command.getDeliveryMethod()).thenReturn(DeliveryMethod.COURIER);

        final Result<DepartmentId, ErrorCode> result = service.resolveTargetDepartmentId(command);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getSuccess()).isNull();
        verifyNoInteractions(pickupPointServicePort);
    }

    @Test
    void shouldLeaveOriginDepartmentUnknownForExternalShipment() {
        when(command.getPickupMethod()).thenReturn(PickupMethod.COURIER);
        when(command.getSource()).thenReturn(ShipmentSource.EXTERNAL);

        final Result<DepartmentId, ErrorCode> result = service.resolveOriginDepartmentId(command);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getSuccess()).isNull();
    }
}
