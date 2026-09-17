package com.warehouse.deliverynetwork.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.domain.vo.DepartmentAddress;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;

import java.util.List;
import java.util.Optional;

public interface DepartmentDirectoryServicePort {

    List<DepartmentNode> getCurrentOperatorDepartments();

    Optional<DepartmentAddress> findDepartmentAddress(final DepartmentId departmentId);
}
