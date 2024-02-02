package com.warehouse.softwareconfiguration.infrastructure.adapter.secondary;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.warehouse.softwareconfiguration.infrastructure.adapter.secondary.document.SoftwareConfigurationDocument;

public interface SoftwareConfigurationDocumentReadRepository
		extends MongoRepository<SoftwareConfigurationDocument, String> {
    Optional<SoftwareConfigurationDocument> findByName(String name);
}
