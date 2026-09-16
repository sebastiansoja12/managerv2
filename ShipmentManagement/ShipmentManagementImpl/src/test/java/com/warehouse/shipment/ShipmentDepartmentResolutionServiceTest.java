package com.warehouse.shipment;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.secondary.DeliveryAreaServicePort;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import com.warehouse.shipment.application.port.secondary.PathFinderServicePort;
import com.warehouse.shipment.application.port.secondary.PickupPointServicePort;
import com.warehouse.shipment.application.service.ShipmentDepartmentResolutionService;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.enumeration.ShipmentSource;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShipmentDepartmentResolutionServiceTest {

    private static final Party RECIPIENT = new Party("Jan", "Kowalski", "jan@example.com", "123456789",
            "Warszawa", "00-001", "Marszałkowska 1", CountryCode.PL);

    private final PathFinderServicePort pathFinderServicePort = mock(PathFinderServicePort.class);

    private final DeliveryAreaServicePort deliveryAreaServicePort = mock(DeliveryAreaServicePort.class);

    private final DepartmentServicePort departmentServicePort = mock(DepartmentServicePort.class);

    private final ShipmentCreateCommand command = mock(ShipmentCreateCommand.class);

    private final ShipmentDepartmentResolutionService service = new ShipmentDepartmentResolutionService(
            mock(PickupPointServicePort.class), departmentServicePort, pathFinderServicePort,
            deliveryAreaServicePort, mock(OperatorContextProvider.class));

    @BeforeEach
    void setUp() {
        when(command.getDeliveryMethod()).thenReturn(DeliveryMethod.COURIER);
    }

    @Test
    void shouldAssignDepartmentWhoseDeliveryAreaCoversRecipient() {
        when(deliveryAreaServicePort.findDeliveryDepartment(any())).thenReturn(Optional.of(new DepartmentId(7L)));

        final Result<DepartmentId, ErrorCode> result = service.resolveTargetDepartmentId(command, RECIPIENT);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getSuccess()).isEqualTo(new DepartmentId(7L));
        verify(pathFinderServicePort, never()).determineDeliveryDepartment(any());
    }

    @Test
    void shouldFallBackToNearestDepartmentWhenNoDeliveryAreaCoversRecipient() {
        when(deliveryAreaServicePort.findDeliveryDepartment(any())).thenReturn(Optional.empty());
        when(pathFinderServicePort.determineDeliveryDepartment(any()))
                .thenReturn(Result.success(new VoronoiResponse(new DepartmentCode("KT1"))));
        when(departmentServicePort.getDepartmentId(new DepartmentCode("KT1"))).thenReturn(new DepartmentId(3L));

        final Result<DepartmentId, ErrorCode> result = service.resolveTargetDepartmentId(command, RECIPIENT);

        assertThat(result.getSuccess()).isEqualTo(new DepartmentId(3L));
    }

    @Test
    void shouldLeaveOriginDepartmentUnknownForExternalShipment() {
        when(command.getPickupMethod()).thenReturn(PickupMethod.COURIER);
        when(command.getSource()).thenReturn(ShipmentSource.EXTERNAL);

        final Result<DepartmentId, ErrorCode> result = service.resolveOriginDepartmentId(command);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getSuccess()).isNull();
        verify(pathFinderServicePort, never()).determineDeliveryDepartment(any());
    }
}
