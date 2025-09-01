package com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftTemplate {

    private String id;
    private String name;
    private String description;

    private LocalTime startTime;
    private LocalTime endTime;
    private int durationMinutes;

    private int minEmployees;
    private int maxEmployees;
    private Set<String> rolesRequired;

    private DepartmentId departmentId;
    private Set<DayOfWeek> daysOfWeek;

    private boolean overtimeAllowed;
    private boolean nightShift;
    private boolean active;
}
