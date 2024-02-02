package com.warehouse.softwareconfiguration.infrastructure.adapter.primary.mapper;


import com.warehouse.softwareconfiguration.domain.model.Software;
import com.warehouse.softwareconfiguration.infrastructure.adapter.primary.dto.SoftwareDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SoftwareResponseMapper {
    SoftwareDto map(Software software);

    List<SoftwareDto> map(List<Software> softwares);
}
