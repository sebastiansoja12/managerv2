package com.warehouse.softwareconfiguration.domain.port.primary;

import com.warehouse.softwareconfiguration.domain.model.Software;
import com.warehouse.softwareconfiguration.domain.vo.SoftwareProperty;

import java.util.List;

public interface SoftwareConfigurationPort {
    Software create(SoftwareProperty property);
    Software get(String name);

    List<Software> findAll();

    Software update(String id, SoftwareProperty softwareProperty);
}
