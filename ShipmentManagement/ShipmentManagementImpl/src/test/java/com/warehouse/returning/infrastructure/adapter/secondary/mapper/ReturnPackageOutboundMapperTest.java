package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.returning.domain.vo.CreateReturnRequest;
import com.warehouse.returning.infrastructure.adapter.secondary.api.ReturnRequestApi;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReturnPackageOutboundMapperTest {

    @Test
    void sendsResolvedDepartmentIdToRtm() {
        final CreateReturnRequest createReturnRequest = new CreateReturnRequest(
                new ShipmentId(123L), "Damaged parcel", new DepartmentId(31L),
                new UserId(41L), ReasonCode.DAMAGED);

        final ReturnRequestApi request = new ReturnPackageOutboundMapper().map(createReturnRequest);

        assertThat(request.requests().getFirst().departmentId().value()).isEqualTo(31L);
    }
}
