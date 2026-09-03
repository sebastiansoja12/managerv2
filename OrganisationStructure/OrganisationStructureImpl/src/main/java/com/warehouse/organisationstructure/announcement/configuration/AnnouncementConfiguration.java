package com.warehouse.organisationstructure.announcement.configuration;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.PlatformActorService;
import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementAudienceServicePort;
import com.warehouse.organisationstructure.announcement.domain.port.secondary.AnnouncementRepository;
import com.warehouse.organisationstructure.announcement.domain.service.AnnouncementService;
import com.warehouse.organisationstructure.announcement.domain.service.AnnouncementServiceImpl;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary.AnnouncementAudienceServiceAdapter;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary.AnnouncementReadRepository;
import com.warehouse.organisationstructure.announcement.infrastructure.adapter.secondary.AnnouncementRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AnnouncementConfiguration {

    @Bean
    public AnnouncementRepository announcementRepository(final AnnouncementReadRepository announcementReadRepository) {
        return new AnnouncementRepositoryImpl(announcementReadRepository);
    }

    @Bean("organisationstructure.announcementAudienceServicePort")
    public AnnouncementAudienceServicePort announcementAudienceServicePort(
            final PlatformActorService platformActorService,
            final CurrentUserApiService currentUserApiService) {
        return new AnnouncementAudienceServiceAdapter(platformActorService, currentUserApiService);
    }

    @Bean
    public AnnouncementService announcementService(final AnnouncementRepository announcementRepository,
                                                   final AnnouncementAudienceServicePort announcementAudienceServicePort) {
        return new AnnouncementServiceImpl(announcementRepository, announcementAudienceServicePort, Clock.systemUTC());
    }
}
