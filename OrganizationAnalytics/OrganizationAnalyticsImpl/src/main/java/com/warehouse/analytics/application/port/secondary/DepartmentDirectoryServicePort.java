package com.warehouse.analytics.application.port.secondary;

import com.warehouse.analytics.domain.vo.AnalyticsDepartment;

import java.util.List;

public interface DepartmentDirectoryServicePort {

    List<AnalyticsDepartment> departments();
}
