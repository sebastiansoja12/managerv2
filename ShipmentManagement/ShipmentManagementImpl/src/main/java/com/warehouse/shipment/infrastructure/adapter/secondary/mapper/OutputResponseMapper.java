package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.domain.vo.VoronoiResponse;
import com.warehouse.voronoi.VoronoiResponseDto;

public abstract class OutputResponseMapper {

    public static VoronoiResponse map(final VoronoiResponseDto response) {
        if (response == null) {
            return null;
        }

        final String departmentCode = response.departmentCode().value();
        return new VoronoiResponse(new DepartmentCode(departmentCode));
    }
}
