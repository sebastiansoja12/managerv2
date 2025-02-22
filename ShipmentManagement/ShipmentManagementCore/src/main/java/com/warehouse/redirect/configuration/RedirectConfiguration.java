package com.warehouse.redirect.configuration;

import com.warehouse.redirect.domain.port.secondary.RedirectTrackerServicePort;
import com.warehouse.redirect.infrastructure.adapter.secondary.RedirectTrackerServiceAdapter;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.redirect.domain.port.primary.RedirectTokenPort;
import com.warehouse.redirect.domain.port.primary.RedirectTokenPortImpl;
import com.warehouse.redirect.domain.port.secondary.MailServicePort;
import com.warehouse.redirect.domain.port.secondary.RedirectTokenRepository;
import com.warehouse.redirect.domain.service.RedirectService;
import com.warehouse.redirect.domain.service.RedirectServiceImpl;
import com.warehouse.redirect.domain.service.RedirectTokenGenerator;
import com.warehouse.redirect.domain.service.RedirectTokenGeneratorImpl;
import com.warehouse.redirect.infrastructure.adapter.secondary.MailAdapter;
import com.warehouse.redirect.infrastructure.adapter.secondary.RedirectTokenReadRepository;
import com.warehouse.redirect.infrastructure.adapter.secondary.RedirectTokenRepositoryImpl;
import com.warehouse.redirect.infrastructure.adapter.secondary.mapper.NotificationMapper;
import com.warehouse.redirect.infrastructure.adapter.secondary.mapper.RedirectTokenMapper;
import com.warehouse.redirect.infrastructure.adapter.secondary.properties.RedirectTokenProperties;

@Configuration
public class RedirectConfiguration {
    
	@Bean
	public RedirectTokenPort redirectTokenPort(RedirectService redirectService, MailServicePort mailServicePort,
                                               final RedirectTrackerServicePort redirectTrackerServicePort) {
		final RedirectTokenGenerator redirectTokenGenerator = new RedirectTokenGeneratorImpl();
		return new RedirectTokenPortImpl(redirectService, redirectTokenGenerator, mailServicePort,
                redirectTrackerServicePort);
	}

    @Bean
    public RedirectTrackerServicePort redirectTrackerServicePort() {
        return new RedirectTrackerServiceAdapter();
    }

    @Bean(name = "redirect.mailServicePort")
    public MailServicePort mailServicePort(MailPort mailPort, RedirectTokenProperties properties) {
        final NotificationMapper mapper = Mappers.getMapper(NotificationMapper.class);
        return new MailAdapter(mailPort, mapper, properties);
    }

    @Bean
    public RedirectTokenProperties properties() {
        return new RedirectTokenProperties();
    }

    @Bean
    public RedirectService redirectService(RedirectTokenRepository redirectTokenRepository) {
        return new RedirectServiceImpl(redirectTokenRepository);
    }

    @Bean
    public RedirectTokenRepository redirectTokenRepository(RedirectTokenReadRepository redirectTokenReadRepository) {
        final RedirectTokenMapper mapper = Mappers.getMapper(RedirectTokenMapper.class);
        return new RedirectTokenRepositoryImpl(redirectTokenReadRepository, mapper);
    }
}
