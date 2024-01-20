package com.warehouse.softwareconfiguration.infrastructure.adapter.primary.mapper;

import com.warehouse.softwareconfiguration.domain.vo.SoftwareProperty;
import com.warehouse.softwareconfiguration.infrastructure.adapter.primary.dto.SoftwarePropertyDto;
import org.mapstruct.Mapper;

@Mapper
public interface SoftwareRequestMapper {
    SoftwareProperty map(SoftwarePropertyDto property);
}
