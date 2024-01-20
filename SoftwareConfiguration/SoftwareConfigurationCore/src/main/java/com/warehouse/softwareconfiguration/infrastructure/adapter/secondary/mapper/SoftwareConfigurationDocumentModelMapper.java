package com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.mapper;

import com.warehouse.softwareconfiguration.domain.model.Software;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.SoftwareConfigurationDocument;
import org.mapstruct.Mapper;

@Mapper
public interface SoftwareConfigurationDocumentModelMapper {
    Software map(SoftwareConfigurationDocument document);
}
