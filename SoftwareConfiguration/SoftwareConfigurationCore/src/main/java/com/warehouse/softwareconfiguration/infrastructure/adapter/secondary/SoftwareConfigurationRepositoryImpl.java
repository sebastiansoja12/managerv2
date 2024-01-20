package com.warehouse.softwareconfiguration.infrastructure.adapter.secondary;

import com.warehouse.softwareconfiguration.domain.model.Software;
import com.warehouse.softwareconfiguration.domain.port.secondary.SoftwareConfigurationRepository;
import com.warehouse.softwareconfiguration.domain.vo.SoftwareProperty;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.SoftwareConfigurationDocument;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.exception.SoftwarePropertyNotFoundException;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.mapper.SoftwareConfigurationDocumentMapper;
import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.mapper.SoftwareConfigurationDocumentModelMapper;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class SoftwareConfigurationRepositoryImpl implements SoftwareConfigurationRepository {

    private final SoftwareConfigurationDocumentReadRepository softwareConfigurationDocumentReadRepository;

	private final SoftwareConfigurationDocumentMapper documentMapper = getMapper(
			SoftwareConfigurationDocumentMapper.class);
    
	private final SoftwareConfigurationDocumentModelMapper modelMapper = getMapper(
			SoftwareConfigurationDocumentModelMapper.class);

    @Override
    public Software create(SoftwareProperty property) {
        final SoftwareConfigurationDocument document = documentMapper.map(property);
        softwareConfigurationDocumentReadRepository.save(document);
        return modelMapper.map(document);
    }

    @Override
    public Software get(String name) {
        return softwareConfigurationDocumentReadRepository
                .findByName(name)
                .map(modelMapper::map)
                .orElseThrow(() -> new SoftwarePropertyNotFoundException(404, "Software property not found"));
    }

    @Override
    public List<Software> findAll() {
		return softwareConfigurationDocumentReadRepository
                .findAll()
                .stream()
                .map(modelMapper::map)
				.collect(Collectors.toList());
    }

    @Override
    public Software update(String id, SoftwareProperty softwareProperty) {
		final SoftwareConfigurationDocument document = softwareConfigurationDocumentReadRepository
                .findById(id)
				.orElseThrow(() -> new SoftwarePropertyNotFoundException(404, "Software property not found"));
        document.updateDocument(softwareProperty);
        softwareConfigurationDocumentReadRepository.save(document);
        return modelMapper.map(document);
    }
}
