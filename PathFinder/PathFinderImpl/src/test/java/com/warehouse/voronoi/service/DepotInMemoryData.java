package com.warehouse.voronoi.service;

import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Department;

import java.util.List;

public class DepotInMemoryData {

    public static List<Department> depots() {

        // WROCŁAW
        final Department department = Department.builder()
                .departmentCode("WRO")
                .coordinates(Coordinates.builder()
                        .lon(51.1271647)
                        .lat(16.9218245)
                        .build())
                .build();

        // WARSZAWA
        final Department department2 = Department.builder()
                .departmentCode("WA1")
                .coordinates(Coordinates.builder()
                        .lon(52.2328001)
                        .lat(20.7741284)
                        .build())
                .build();

        // LUBLIN
        final Department department3 = Department.builder()
                .departmentCode("LUB")
                .coordinates(Coordinates.builder()
                        .lon(51.2181956)
                        .lat(22.4937308)
                        .build())
                .build();

        // GLIWICE
        final Department department4 = Department.builder()
                .departmentCode("KT1")
                .coordinates(Coordinates.builder()
                        .lon(50.3013283)
                        .lat(18.5795769)
                        .build())
                .build();

        // POZNAŃ
        final Department department5 = Department.builder()
                .departmentCode("POZ")
                .coordinates(Coordinates.builder()
                        .lon(52.4080281)
                        .lat(16.7615841)
                        .build())
                .build();

        // KRAKÓW
        final Department department6 = Department.builder()
                .departmentCode("KR1")
                .coordinates(Coordinates.builder()
                        .lon(50.0468548)
                        .lat(19.9348336)
                        .build())
                .build();

        return List.of(department, department2, department3, department4, department5, department6);
    }
}
