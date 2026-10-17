package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.enumeration.ReturnStatus;
import com.warehouse.returning.domain.vo.CreatedReturn;
import com.warehouse.returning.infrastructure.adapter.secondary.api.RtmCreateResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReturnPackageResponseMapperTest {

    @Test
    void shouldMapEveryCreatedReturnFromRtmResponse() {
        final RtmCreateResponse response = new RtmCreateResponse(List.of(
                new RtmCreateResponse.CreatedReturnApi(
                        new RtmCreateResponse.ShipmentIdApi(11L),
                        new RtmCreateResponse.ReturnIdApi(21L),
                        "CREATED"),
                new RtmCreateResponse.CreatedReturnApi(
                        new RtmCreateResponse.ShipmentIdApi(12L),
                        new RtmCreateResponse.ReturnIdApi(22L),
                        "CREATED")
        ));

        final List<CreatedReturn> result = new ReturnPackageResponseMapper().map(response);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).shipmentId().getValue()).isEqualTo(11L);
        assertThat(result.get(0).returnId().value()).isEqualTo(21L);
        assertThat(result.get(0).status()).isEqualTo(ReturnStatus.CREATED);
        assertThat(result.get(1).shipmentId().getValue()).isEqualTo(12L);
        assertThat(result.get(1).returnId().value()).isEqualTo(22L);
    }
}
