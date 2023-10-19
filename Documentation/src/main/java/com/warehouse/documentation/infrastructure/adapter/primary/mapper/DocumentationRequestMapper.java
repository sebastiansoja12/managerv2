package com.warehouse.documentation.infrastructure.adapter.primary.mapper;

import com.warehouse.documentation.domain.model.DocumentationRequest;
import com.warehouse.documentation.infrastructure.adapter.primary.api.dto.DocumentationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DocumentationRequestMapper {

    @Mapping(target = "parcel.id", source = "parcel.parcelId.value")
    DocumentationRequest map(DocumentationRequestDto documentationRequest);
}
