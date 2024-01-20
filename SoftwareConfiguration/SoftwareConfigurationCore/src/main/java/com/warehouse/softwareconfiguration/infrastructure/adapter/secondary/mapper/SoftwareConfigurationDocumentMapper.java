package com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.mapper;

import com.warehouse.softwareconfiguration.domain.vo.SoftwareProperty;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.SoftwareConfigurationDocument;
import org.mapstruct.Mapper;

@Mapper
public interface SoftwareConfigurationDocumentMapper {
    SoftwareConfigurationDocument map(SoftwareProperty software);
}

