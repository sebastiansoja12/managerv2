package com.warehouse.organisationstructure.deliveryarea.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.util.Map;

public record DeliveryAreaPolicies(DeliveryAreaRules defaults, Map<DepartmentId, DeliveryAreaRules> departments) {

    public DeliveryAreaPolicies {
        departments = Map.copyOf(departments);
    }

    public static DeliveryAreaPolicies uniform(final DeliveryAreaRules rules) {
        return new DeliveryAreaPolicies(rules, Map.of());
    }

    public DeliveryAreaRules forDepartment(final DepartmentId departmentId) {
        return departments.getOrDefault(departmentId, defaults);
    }
}
