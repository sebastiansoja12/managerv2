package com.warehouse.mail.application.service;

import com.warehouse.mail.application.port.secondary.MailAttachmentServicePort;
import com.warehouse.mail.domain.exception.MailException;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.mail.domain.vo.MailAttachment;

import java.util.List;

public class MailAttachmentResolver {

    private final MailAttachmentServicePort mailAttachmentServicePort;

    public MailAttachmentResolver(final MailAttachmentServicePort mailAttachmentServicePort) {
        this.mailAttachmentServicePort = mailAttachmentServicePort;
    }

    public List<MailAttachment> resolve(final MailRequest request) {
        return request.attachments().stream()
                .map(reference -> this.mailAttachmentServicePort.find(reference)
                        .orElseThrow(() -> new MailException("Mail attachment " + reference.documentId()
                                + " of template " + request.templateKey() + " was not found")))
                .toList();
    }
}
