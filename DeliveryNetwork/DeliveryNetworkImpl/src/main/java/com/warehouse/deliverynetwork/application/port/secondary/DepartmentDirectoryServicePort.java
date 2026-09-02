package com.warehouse.deliverynetwork.application.port.secondary;

import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;

import java.util.List;

public interface DepartmentDirectoryServicePort {

    List<DepartmentNode> getCurrentOperatorDepartments();
}
