package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.identificator.DepartmentId;

public interface Account {

    String getUsername();

    String getPassword();

    String getFirstName();

    String getLastName();

    String getEmail();

    Boolean isDeleted();

    AccountLevel getLevel();

    boolean canAccessOperatorPanel();

    boolean canAccessDepartment(final DepartmentId departmentId);
}
