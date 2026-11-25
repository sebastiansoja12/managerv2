package com.warehouse.mail.infrastructure.adapter.secondary;

import com.warehouse.document.api.DocumentStorageApiService;
import com.warehouse.mail.application.port.secondary.MailAttachmentServicePort;
import com.warehouse.mail.domain.vo.MailAttachment;
import com.warehouse.mail.domain.vo.MailAttachmentReference;

import java.util.Optional;

public class MailAttachmentServiceAdapter implements MailAttachmentServicePort {

    private final DocumentStorageApiService documentStorageApiService;

    public MailAttachmentServiceAdapter(final DocumentStorageApiService documentStorageApiService) {
        this.documentStorageApiService = documentStorageApiService;
    }

    @Override
    public Optional<MailAttachment> find(final MailAttachmentReference reference) {
        return this.documentStorageApiService.findFile(reference.documentId())
                .map(file -> new MailAttachment(file.metadata().filename(), file.metadata().contentType(),
                        file.content()));
    }
}
