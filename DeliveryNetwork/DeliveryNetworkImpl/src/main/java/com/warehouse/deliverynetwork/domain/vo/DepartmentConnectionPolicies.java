package com.warehouse.deliverynetwork.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.util.Map;

public record DepartmentConnectionPolicies(DepartmentConnectionRules defaults,
                                           Map<DepartmentId, DepartmentConnectionRules> departments) {

    public DepartmentConnectionPolicies {
        departments = Map.copyOf(departments);
    }

    public static DepartmentConnectionPolicies uniform(final DepartmentConnectionRules rules) {
        return new DepartmentConnectionPolicies(rules, Map.of());
    }

    public DepartmentConnectionRules forDepartment(final DepartmentId departmentId) {
        return departments.getOrDefault(departmentId, defaults);
    }
}
