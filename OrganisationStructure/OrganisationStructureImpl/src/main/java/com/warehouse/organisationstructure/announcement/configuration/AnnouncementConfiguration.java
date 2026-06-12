package com.warehouse.organisationstructure.announcement.configuration;

import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementRepository;
import com.warehouse.organisationstructure.announcement.domain.service.AnnouncementService;
import com.warehouse.organisationstructure.announcement.domain.service.AnnouncementServiceImpl;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary.AnnouncementReadRepository;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary.AnnouncementRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnnouncementConfiguration {

    @Bean
    public AnnouncementRepository announcementRepository(final AnnouncementReadRepository announcementReadRepository) {
        return new AnnouncementRepositoryImpl(announcementReadRepository);
    }

    @Bean
    public AnnouncementService announcementService(final AnnouncementRepository announcementRepository) {
        return new AnnouncementServiceImpl(announcementRepository);
    }
}
