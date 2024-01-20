package com.warehouse.softwareconfiguration.domain.port.primary;

import com.warehouse.softwareconfiguration.domain.model.Software;
import com.warehouse.softwareconfiguration.domain.port.secondary.SoftwareConfigurationRepository;
import com.warehouse.softwareconfiguration.domain.vo.SoftwareProperty;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SoftwareConfigurationPortImpl implements SoftwareConfigurationPort {

    private final SoftwareConfigurationRepository softwareConfigurationRepository;

    @Override
    public Software create(SoftwareProperty property) {
        return softwareConfigurationRepository.create(property);
    }

    @Override
    public Software get(String name) {
        return softwareConfigurationRepository.get(name);
    }

    @Override
    public List<Software> findAll() {
        return softwareConfigurationRepository.findAll();
    }

    @Override
    public Software update(String id, SoftwareProperty softwareProperty) {
        return softwareConfigurationRepository.update(id, softwareProperty);
    }
}
