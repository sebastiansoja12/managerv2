package com.warehouse.returning;

import com.warehouse.returning.domain.service.ReturnTokenGeneratorServiceImpl;
import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.ReturnToken;
import com.warehouse.returning.domain.vo.ShipmentId;
import com.warehouse.returning.domain.vo.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReturnTokenGeneratorServiceImplTest {

    private final ReturnTokenGeneratorServiceImpl returnTokenGeneratorService = new ReturnTokenGeneratorServiceImpl();


    @Test
    void shouldGenerateSixDigitReturnToken() {
        final ShipmentId shipmentId = new ShipmentId(1234567L);
        final DepartmentId departmentId = new DepartmentId(5L);
        final UserId userId = new UserId(1L);


        final ReturnToken token = returnTokenGeneratorService.generateToken(shipmentId, departmentId, userId);

        assertEquals(6, token.value().length());
    }
}
