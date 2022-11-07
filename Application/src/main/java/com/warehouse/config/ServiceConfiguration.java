package com.warehouse.config;

import com.warehouse.repository.ParcelRepository;
import com.warehouse.repository.TemporaryRerouteTokenRepository;
import com.warehouse.service.MailContentBuilder;
import com.warehouse.service.MailService;
import com.warehouse.service.TemporaryReroute;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {



    @Bean
    public MailContentBuilder mailContentBuilder() {
        return new MailContentBuilder();
    }


    @Bean
    public TemporaryReroute temporaryReroute( TemporaryRerouteTokenRepository temporaryRerouteTokenRepository,
                                              ParcelRepository parcelRepository,
                                              MailService mailService) {
        return new TemporaryReroute(temporaryRerouteTokenRepository, parcelRepository, mailService);
    }

}