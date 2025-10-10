package com.warehouse.returning.configuration;

import com.warehouse.returning.domain.service.ReturnTokenGeneratorServiceImpl;
import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReturnTokenGeneratorServiceImplTest {

    private final ReturnTokenGeneratorServiceImpl returnTokenGeneratorService = new ReturnTokenGeneratorServiceImpl();


    @Test
    void shouldGenerateSixDigitReturnToken() {
        final ShipmentId shipmentId = new ShipmentId(1234567L);
        final DepartmentCode departmentCode = new DepartmentCode("TST");
        final UserId userId = new UserId(1L);


        final ReturnToken token = returnTokenGeneratorService.generateToken(shipmentId, departmentCode, userId);

        assertEquals(6, token.value().length());
    }
}
