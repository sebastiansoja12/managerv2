package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.recipient;
import static com.warehouse.shipment.DataTestCreator.sender;
import static com.warehouse.shipment.DataTestCreator.shipmentId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.port.secondary.CurrentUserServicePort;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.shipment.domain.service.RouteTrackerServiceImpl;
import com.warehouse.shipment.domain.vo.Person;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.domain.vo.SoftwareConfiguration;
import com.warehouse.shipment.domain.vo.UserContext;

@ExtendWith(MockitoExtension.class)
class RouteTrackerServiceImplTest {

    @Mock
    private RouteLogServicePort routeLogServicePort;

    @Mock
    private SoftwareConfigurationServicePort softwareConfigurationServicePort;

    @Mock
    private CurrentUserServicePort currentUserServicePort;

    @Test
    void shouldNotifyShipmentCreated() {
        final RouteTrackerServiceImpl service = service();
        final ShipmentId shipmentId = shipmentId();
        final SoftwareConfiguration softwareConfiguration = softwareConfiguration();
        final RouteProcess routeProcess = routeProcess(shipmentId);
        final UserId userId = new UserId(1L);
        when(softwareConfigurationServicePort.getShipmentSoftwareConfiguration()).thenReturn(softwareConfiguration);
        when(currentUserServicePort.getCurrentUserContext())
                .thenReturn(new UserContext(userId, new DepartmentCode("KT1")));
        when(routeLogServicePort.notifyShipmentCreated(shipmentId, softwareConfiguration, userId))
                .thenReturn(Result.success(routeProcess));

        final Result<RouteProcess, ErrorCode> result = service.notifyShipmentCreated(shipmentId);

        assertTrue(result.isSuccess());
        assertEquals(routeProcess, result.getSuccess());
        verify(routeLogServicePort).notifyShipmentCreated(shipmentId, softwareConfiguration, userId);
    }

    @Test
    void shouldReturnSuccessWhenRecipientChangeIsNotified() {
        final RouteTrackerServiceImpl service = service();
        final ShipmentId shipmentId = shipmentId();
        final Recipient recipient = recipient();
        final SoftwareConfiguration softwareConfiguration = softwareConfiguration();
        final RouteProcess routeProcess = routeProcess(shipmentId);
        when(softwareConfigurationServicePort.getShipmentPersonSoftwareConfiguration()).thenReturn(softwareConfiguration);
        when(routeLogServicePort.notifyPersonChanged(shipmentId, recipient, softwareConfiguration))
                .thenReturn(routeProcess);

        final Result<RouteProcess, ErrorCode> result = service.notifyShipmentRecipientChanged(shipmentId, recipient);

        assertTrue(result.isSuccess());
        assertEquals(routeProcess, result.getSuccess());
        verify(routeLogServicePort).notifyPersonChanged(shipmentId, recipient, softwareConfiguration);
    }

    @Test
    void shouldReturnFailureWhenRecipientChangeCannotBeNotified() {
        final RouteTrackerServiceImpl service = service();
        final ShipmentId shipmentId = shipmentId();
        final Recipient recipient = recipient();
        final SoftwareConfiguration softwareConfiguration = softwareConfiguration();
        when(softwareConfigurationServicePort.getShipmentPersonSoftwareConfiguration()).thenReturn(softwareConfiguration);
        when(routeLogServicePort.notifyPersonChanged(shipmentId, recipient, softwareConfiguration))
                .thenReturn(null);

        final Result<RouteProcess, ErrorCode> result = service.notifyShipmentRecipientChanged(shipmentId, recipient);

        assertTrue(result.isFailure());
        assertEquals(ErrorCode.ROUTE_TRACKER_SERVICE_NOT_AVAILABLE, result.getFailure());
    }

    @Test
    void shouldReturnSuccessWhenPersonChangeIsNotified() {
        final RouteTrackerServiceImpl service = service();
        final ShipmentId shipmentId = shipmentId();
        final Person person = sender();
        final SoftwareConfiguration softwareConfiguration = softwareConfiguration();
        final RouteProcess routeProcess = routeProcess(shipmentId);
        when(softwareConfigurationServicePort.getShipmentPersonSoftwareConfiguration()).thenReturn(softwareConfiguration);
        when(routeLogServicePort.notifyPersonChanged(shipmentId, person, softwareConfiguration))
                .thenReturn(routeProcess);

        final Result<RouteProcess, ErrorCode> result = service.notifyShipmentPersonChanged(shipmentId, person);

        assertTrue(result.isSuccess());
        assertEquals(routeProcess, result.getSuccess());
    }

    @Test
    void shouldReturnSuccessWhenShipmentStatusChanged() {
        final RouteTrackerServiceImpl service = service();

        final Result<RouteProcess, ErrorCode> result =
                service.notifyShipmentStatusChanged(shipmentId(), ShipmentStatus.SENT);

        assertTrue(result.isSuccess());
    }

    private SoftwareConfiguration softwareConfiguration() {
        return new SoftwareConfiguration("shipment", "http://route-tracker");
    }

    private RouteTrackerServiceImpl service() {
        return new RouteTrackerServiceImpl(routeLogServicePort, softwareConfigurationServicePort, currentUserServicePort);
    }

    private RouteProcess routeProcess(final ShipmentId shipmentId) {
        return RouteProcess.from(shipmentId, new ProcessId(UUID.randomUUID()), null, "OK");
    }
}
