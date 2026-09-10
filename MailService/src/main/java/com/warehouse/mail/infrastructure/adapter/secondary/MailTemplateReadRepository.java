package com.warehouse.mail.infrastructure.adapter.secondary;

import com.warehouse.mail.infrastructure.adapter.secondary.entity.MailTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MailTemplateReadRepository extends JpaRepository<MailTemplateEntity, UUID> {

    Optional<MailTemplateEntity> findByTemplateKeyAndLocale(final String templateKey, final String locale);
}
