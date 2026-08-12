package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.domain.port.secondary.DepartmentServicePort;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.port.secondary.UserServicePort;
import com.warehouse.shipment.domain.service.RouteLogServiceImpl;
import com.warehouse.shipment.domain.vo.RouteLogRecord;
import com.warehouse.shipment.domain.vo.RouteLogRecordDetail;
import com.warehouse.shipment.domain.vo.RouteLogRecordDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteLogServiceImplTest {

    @Mock
    private RouteLogServicePort routeLogServicePort;

    @Mock
    private DepartmentServicePort departmentServicePort;

    @Mock
    private UserServicePort userServicePort;

    @Test
    void shouldResolveUsernameAndDepartmentCodeFromIdentifiers() {
        final ShipmentId shipmentId = new ShipmentId(123L);
        final UserId userId = new UserId(42L);
        final DepartmentId departmentId = new DepartmentId(10L);
        final RouteLogRecord routeLog = new RouteLogRecord(null, shipmentId,
                new RouteLogRecordDetails(Set.of(
                        detail(1L, userId, departmentId),
                        detail(2L, userId, departmentId))),
                null, null);
        final RouteLogServiceImpl service =
                new RouteLogServiceImpl(routeLogServicePort, departmentServicePort, userServicePort);
        when(routeLogServicePort.findByShipmentId(shipmentId)).thenReturn(routeLog);
        when(departmentServicePort.getDepartmentCode(departmentId)).thenReturn(new DepartmentCode("WAW01"));
        when(userServicePort.getUsername(userId)).thenReturn("jan.kowalski");

        final RouteLogRecord resolved = service.findByShipmentId(shipmentId).orElseThrow();

        assertEquals(2, resolved.routeLogRecordDetails().routeLogRecordDetailSet().size());
        resolved.routeLogRecordDetails().routeLogRecordDetailSet().forEach(detail -> {
            assertEquals(userId, detail.userId());
            assertEquals("jan.kowalski", detail.username());
            assertEquals(departmentId, detail.departmentId());
            assertEquals("WAW01", detail.departmentCode());
        });
        verify(userServicePort, times(1)).getUsername(userId);
        verify(departmentServicePort, times(1)).getDepartmentCode(departmentId);
    }

    private RouteLogRecordDetail detail(
            final Long id,
            final UserId userId,
            final DepartmentId departmentId) {
        return new RouteLogRecordDetail(id, null, null, userId, null, null, departmentId, null,
                null, null, null, null, null);
    }
}
