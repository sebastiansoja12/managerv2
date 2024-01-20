package com.warehouse.softwareconfiguration.domain.port.secondary;

import com.warehouse.softwareconfiguration.domain.model.Software;
import com.warehouse.softwareconfiguration.domain.vo.SoftwareProperty;

import java.util.List;

public interface SoftwareConfigurationRepository {
    Software create(SoftwareProperty property);

    Software get(String name);

    List<Software> findAll();

    Software update(String id, SoftwareProperty softwareProperty);
}
