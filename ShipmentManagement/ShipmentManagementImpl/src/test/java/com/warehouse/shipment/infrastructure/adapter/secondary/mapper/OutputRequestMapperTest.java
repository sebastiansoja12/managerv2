package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.vo.ShipmentReturnedCommand;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnPackageResponseApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnRequestApi;

class OutputRequestMapperTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldMapDepartmentCodeFromShipmentReturnedCommand() {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernameTenantPasswordAuthenticationToken(
                new UserId(11L),
                OperatorId.of(10001L),
                "token",
                List.of()
        ));
        SecurityContextHolder.setContext(securityContext);
        final ShipmentReturnedCommand command = new ShipmentReturnedCommand(
                new ShipmentId(582104L),
                ReasonCode.DAMAGED,
                "Damaged package",
                new DepartmentCode("KT1")
        );

        final ReturnRequestApi returnRequest = OutputRequestMapper.map(command);

        final ReturnPackageResponseApi returnPackage = returnRequest.requests().getFirst();
        assertEquals("KT1", returnPackage.departmentCode().value());
    }
}
