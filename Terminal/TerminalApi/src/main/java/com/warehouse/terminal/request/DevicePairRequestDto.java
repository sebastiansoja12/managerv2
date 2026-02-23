package com.warehouse.terminal.request;


import com.warehouse.terminal.dto.DepartmentCodeDto;
import com.warehouse.terminal.dto.UserIdDto;

public record DevicePairRequestDto(String externalSystemId, DepartmentCodeDto departmentCode, UserIdDto userId) {

}