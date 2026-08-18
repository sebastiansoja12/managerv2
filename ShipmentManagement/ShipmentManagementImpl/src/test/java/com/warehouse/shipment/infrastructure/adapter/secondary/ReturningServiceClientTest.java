package com.warehouse.shipment.infrastructure.adapter.secondary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ReturnId;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.enumeration.ReturnStatus;
import com.warehouse.shipment.domain.vo.ShipmentReturnDetails;
import com.warehouse.shipment.domain.vo.ShipmentReturnPage;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.DepartmentCodeApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReasonCodeApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnIdDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnPackageApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnPageApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnTokenApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.UserIdApi;
import com.warehouse.tools.returning.ReturnProperties;

@ExtendWith(MockitoExtension.class)
class ReturningServiceClientTest {

    private static final URI RETURN_URI = URI.create("http://returning-track-manager/returns/123");

    @Mock
    private ExternalFeignClient externalFeignClient;

    private ReturningServiceClient returningServiceClient;

    @BeforeEach
    void setUp() {
        final ReturnProperties returnProperties = new ReturnProperties();
        returnProperties.setUrl("http://returning-track-manager");
        returnProperties.setEndpoint("/returns");
        returningServiceClient = new ReturningServiceClient(externalFeignClient, returnProperties);
    }

    @Test
    void shouldLoadReturnFromReturningTrackManager() {
        final Instant createdAt = Instant.parse("2026-08-14T10:00:00Z");
        final Instant updatedAt = Instant.parse("2026-08-14T11:00:00Z");
        final ReturnPackageApi response = new ReturnPackageApi(
                new ReturnIdDto(123L),
                new ShipmentIdDto(456L),
                "Damaged parcel",
                "PROCESSING",
                new ReturnTokenApi("token-123"),
                new DepartmentCodeApi("KT1"),
                null,
                new UserIdApi(10L),
                null,
                new ReasonCodeApi("DAMAGED"),
                77L,
                createdAt,
                updatedAt);
        when(externalFeignClient.getReturn(RETURN_URI)).thenReturn(ResponseEntity.ok(response));

        final ShipmentReturnDetails result = returningServiceClient.getReturn(new ReturnId(123L));

        assertEquals(123L, result.returnPackageId().getId());
        assertEquals(Long.valueOf(456L), result.shipmentId().getValue());
        assertEquals("Damaged parcel", result.reason());
        assertEquals(ReturnStatus.PROCESSING, result.returnStatus());
        assertEquals("token-123", result.returnToken());
        assertEquals("KT1", result.assignedDepartmentCode().value());
        assertNull(result.returnedDepartmentCode());
        assertEquals(Long.valueOf(10L), result.assignedTo().value());
        assertNull(result.processedBy());
        assertEquals(ReasonCode.DAMAGED, result.reasonCode());
        assertEquals(77L, result.operatorId());
        assertEquals(createdAt, result.createdAt());
        assertEquals(updatedAt, result.updatedAt());
        verify(externalFeignClient).getReturn(RETURN_URI);
    }

    @Test
    void shouldLoadReturnsPageForDepartmentThroughReturningTrackManager() {
        final URI returnsUri = URI.create(
                "http://returning-track-manager/returns?departmentCode=KT1&page=0&size=50");
        final ReturnPackageApi returnPackage = new ReturnPackageApi(
                new ReturnIdDto(123L),
                new ShipmentIdDto(456L),
                "Damaged parcel",
                "CREATED",
                new ReturnTokenApi("token-123"),
                new DepartmentCodeApi("KT1"),
                null,
                new UserIdApi(10L),
                null,
                new ReasonCodeApi("DAMAGED"),
                77L,
                Instant.parse("2026-08-14T10:00:00Z"),
                Instant.parse("2026-08-14T11:00:00Z"));
        when(externalFeignClient.getReturns(returnsUri)).thenReturn(ResponseEntity.ok(
                new ReturnPageApi(List.of(returnPackage), 0, 50, 1L, 1)));

        final ShipmentReturnPage result = returningServiceClient.getReturns(new DepartmentCode("KT1"), 0, 50);

        assertEquals(1L, result.totalElements());
        assertEquals(123L, result.content().get(0).returnPackageId().getId());
        assertEquals(77L, result.content().get(0).operatorId());
        verify(externalFeignClient).getReturns(returnsUri);
    }
}
