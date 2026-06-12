package com.warehouse.returning.infrastructure.adapter.primary.mapper;

import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.vo.DecodedApiOperator;
import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.UserId;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RequestMapperTest {

    @Test
    void shouldMapOperatorIdFromAuthenticatedOperator() {
        final ReturnRequestApi apiRequest = new ReturnRequestApi(List.of(new ReturnPackageRequestApi(
                new ShipmentIdApi(123L),
                "Damaged parcel",
                new DepartmentCodeApi("WAW01"),
                new UserIdApi(41L),
                new ReasonCodeApi("DAMAGED"))));
        final DecodedApiOperator decodedApiOperator = new DecodedApiOperator(
                new UserId(12L), new DepartmentCode("KT1"), 77L, "operator");

        final ReturnRequest result = RequestMapper.map(apiRequest, decodedApiOperator);

        assertThat(result.getOperatorId()).isEqualTo(77L);
        assertThat(result.getIssuerDepartmentCode().value()).isEqualTo("KT1");
    }
}
