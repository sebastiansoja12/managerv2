package com.warehouse.logisticsdelivery.infrastructure.adapter.primary;

import com.warehouse.logisticsdelivery.application.port.primary.DepartmentWorkloadPort;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.api.DepartmentLoadApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deliveries/department-loads")
public class DepartmentLoadController {

    private final DepartmentWorkloadPort departmentWorkloadPort;

    public DepartmentLoadController(final DepartmentWorkloadPort departmentWorkloadPort) {
        this.departmentWorkloadPort = departmentWorkloadPort;
    }

    @GetMapping
    public List<DepartmentLoadApiResponse> departmentLoads() {
        return this.departmentWorkloadPort.findDepartmentLoads().stream()
                .map(load -> new DepartmentLoadApiResponse(load.departmentId().getValue(), load.deliveriesToday(),
                        load.dailyCapacity(), load.utilizationPercent(), load.warning(), load.overloaded()))
                .toList();
    }
}
