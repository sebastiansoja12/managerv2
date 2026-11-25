package com.warehouse.mail.application.port.secondary;

import com.warehouse.mail.domain.vo.MailAttachment;
import com.warehouse.mail.domain.vo.MailAttachmentReference;

import java.util.Optional;

public interface MailAttachmentServicePort {
    Optional<MailAttachment> find(final MailAttachmentReference reference);
}
