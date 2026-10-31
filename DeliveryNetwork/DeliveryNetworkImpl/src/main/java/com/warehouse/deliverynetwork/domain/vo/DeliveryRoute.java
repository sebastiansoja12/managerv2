package com.warehouse.deliverynetwork.domain.vo;

import java.util.List;
import java.util.Optional;

public record DeliveryRoute(List<DepartmentNode> departments) {

    public DeliveryRoute {
        departments = List.copyOf(departments);
    }

    public Optional<Double> distanceInKilometers() {
        if (this.departments.stream().anyMatch(department -> department.location() == null)) {
            return Optional.empty();
        }
        double distanceInKilometers = 0;
        for (int index = 1; index < this.departments.size(); index++) {
            distanceInKilometers += this.departments.get(index - 1).distanceInKilometersTo(this.departments.get(index));
        }
        return Optional.of(distanceInKilometers);
    }
}
