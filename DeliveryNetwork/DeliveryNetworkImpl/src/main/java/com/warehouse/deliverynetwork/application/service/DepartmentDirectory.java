package com.warehouse.deliverynetwork.application.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.application.exception.DepartmentDirectoryImportMismatchException;
import com.warehouse.deliverynetwork.application.exception.IncompleteDepartmentDirectoryImportException;
import com.warehouse.deliverynetwork.application.exception.UnknownDepartmentCodeException;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentConnectionCodeCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentImportCommand;
import com.warehouse.deliverynetwork.domain.exception.UnknownDepartmentException;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DepartmentDirectory {

    private final List<DepartmentNode> departments;

    private Map<String, DepartmentNode> departmentsByCode;

    private Map<DepartmentId, DepartmentCode> departmentCodesById;

    public DepartmentDirectory(final List<DepartmentNode> departments) {
        this.departments = List.copyOf(departments);
    }

    public List<DepartmentNode> departments() {
        return this.departments;
    }

    public DepartmentNode department(final DepartmentCode departmentCode) {
        final DepartmentNode department = departmentsByCode().get(normalizedCode(departmentCode));
        if (department == null) {
            throw new UnknownDepartmentCodeException(departmentCode);
        }
        return department;
    }

    public DepartmentCode departmentCode(final DepartmentId departmentId) {
        final DepartmentCode departmentCode = departmentCodesById().get(departmentId);
        if (departmentCode == null) {
            throw new UnknownDepartmentException(departmentId);
        }
        return departmentCode;
    }

    public DepartmentConnection connection(final DepartmentConnectionCodeCommand connection,
                                           final boolean bidirectionalByDefault) {
        return new DepartmentConnection(
                department(connection.firstDepartmentCode()).departmentId(),
                department(connection.secondDepartmentCode()).departmentId(),
                connection.bidirectional() == null ? bidirectionalByDefault : connection.bidirectional());
    }

    public void validateImport(final List<DepartmentImportCommand> importedDepartments) {
        final Set<String> importedCodes = importedDepartments.stream()
                .map(importedDepartment -> {
                    final DepartmentNode department = department(importedDepartment.departmentCode());
                    if (department.departmentType() != importedDepartment.departmentType()
                            || department.status() != importedDepartment.status()) {
                        throw new DepartmentDirectoryImportMismatchException(importedDepartment.departmentCode());
                    }
                    return normalizedCode(importedDepartment.departmentCode());
                })
                .collect(Collectors.toSet());
        final List<DepartmentCode> missingDepartmentCodes = this.departments.stream()
                .map(DepartmentNode::departmentCode)
                .filter(departmentCode -> !importedCodes.contains(normalizedCode(departmentCode)))
                .toList();
        if (!missingDepartmentCodes.isEmpty()) {
            throw new IncompleteDepartmentDirectoryImportException(missingDepartmentCodes);
        }
    }

    private Map<String, DepartmentNode> departmentsByCode() {
        if (this.departmentsByCode == null) {
            final Map<String, DepartmentNode> index = new HashMap<>();
            for (final DepartmentNode department : this.departments) {
                if (index.put(normalizedCode(department.departmentCode()), department) != null) {
                    throw new IllegalArgumentException(
                            "Department directory contains duplicate code: " + department.departmentCode().getValue());
                }
            }
            this.departmentsByCode = index;
        }
        return this.departmentsByCode;
    }

    private Map<DepartmentId, DepartmentCode> departmentCodesById() {
        if (this.departmentCodesById == null) {
            final Map<DepartmentId, DepartmentCode> index = new HashMap<>();
            for (final DepartmentNode department : this.departments) {
                if (index.put(department.departmentId(), department.departmentCode()) != null) {
                    throw new IllegalArgumentException(
                            "Department directory contains duplicate ID: " + department.departmentId().getValue());
                }
            }
            this.departmentCodesById = index;
        }
        return this.departmentCodesById;
    }

    private static String normalizedCode(final DepartmentCode departmentCode) {
        return departmentCode.getValue().trim().toUpperCase(Locale.ROOT);
    }
}
