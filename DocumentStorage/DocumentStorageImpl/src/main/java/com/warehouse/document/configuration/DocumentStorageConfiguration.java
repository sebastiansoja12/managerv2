package com.warehouse.document.configuration;

import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.document.api.DocumentStorageApiService;
import com.warehouse.document.application.port.primary.DocumentStoragePort;
import com.warehouse.document.application.port.primary.DocumentStoragePortImpl;
import com.warehouse.document.application.port.secondary.DocumentStorageRepository;
import com.warehouse.document.infrastructure.adapter.primary.DocumentStorageServiceAdapter;
import com.warehouse.document.infrastructure.adapter.primary.mapper.DocumentStorageApiMapper;
import com.warehouse.document.infrastructure.adapter.secondary.DocumentStorageRepositoryImpl;
import com.warehouse.document.infrastructure.adapter.secondary.entity.StoredDocumentEntity;
import com.warehouse.document.infrastructure.adapter.secondary.mapper.DocumentStoragePersistenceMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentStorageConfiguration {

    @Bean
    public DocumentStoragePort documentStoragePort(final DocumentStorageRepository repository) {
        return new DocumentStoragePortImpl(repository);
    }

    @Bean
    public DocumentStorageApiService documentStorageApiService(final DocumentStoragePort documentStoragePort) {
        return new DocumentStorageServiceAdapter(documentStoragePort, new DocumentStorageApiMapper());
    }

    @Bean
    public DocumentStorageRepository documentStorageRepository(
            final OperatorFilteredRepository<StoredDocumentEntity> repository) {
        return new DocumentStorageRepositoryImpl(repository, new DocumentStoragePersistenceMapper());
    }
}
