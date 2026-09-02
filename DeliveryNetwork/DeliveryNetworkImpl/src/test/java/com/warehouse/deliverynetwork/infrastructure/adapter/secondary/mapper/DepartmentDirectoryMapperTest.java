package com.warehouse.deliverynetwork.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.department.api.dto.CoordinatesDto;
import com.warehouse.department.api.dto.DepartmentCodeDto;
import com.warehouse.department.api.dto.DepartmentDirectoryEntryDto;
import com.warehouse.department.api.dto.DepartmentIdDto;
import com.warehouse.department.api.dto.DepartmentStatusDto;
import com.warehouse.department.api.dto.DepartmentTypeDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepartmentDirectoryMapperTest {

    private final DepartmentDirectoryMapper departmentDirectoryMapper = new DepartmentDirectoryMapper();

    @Test
    void shouldMapDepartmentApiContractToDeliveryNetworkDomain() {
        final DepartmentDirectoryEntryDto department = new DepartmentDirectoryEntryDto(
                new DepartmentIdDto(1L),
                new DepartmentCodeDto("NCS"),
                DepartmentTypeDto.SORTING_FACILITY,
                DepartmentStatusDto.ACTIVE,
                new CoordinatesDto(52.2297, 21.0122));

        final DepartmentNode departmentNode = this.departmentDirectoryMapper.toModel(department);

        assertEquals(new DepartmentId(1L), departmentNode.departmentId());
        assertEquals(new DepartmentCode("NCS"), departmentNode.departmentCode());
        assertEquals(DepartmentType.SORTING_FACILITY, departmentNode.departmentType());
        assertEquals(DepartmentStatus.ACTIVE, departmentNode.status());
    }
}
