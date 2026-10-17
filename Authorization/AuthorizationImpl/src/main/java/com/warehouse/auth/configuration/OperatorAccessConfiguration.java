package com.warehouse.auth.configuration;

import com.warehouse.auth.OperatorSessionApiService;
import com.warehouse.auth.domain.port.primary.OperatorSessionPort;
import com.warehouse.auth.domain.port.primary.OperatorSessionPortImpl;
import com.warehouse.auth.domain.port.secondary.OperatorSessionRepository;
import com.warehouse.auth.domain.port.secondary.OperatorStatusServicePort;
import com.warehouse.auth.domain.service.OperatorAccessPolicy;
import com.warehouse.auth.domain.service.OperatorAccessPolicyImpl;
import com.warehouse.auth.infrastructure.adapter.primary.OperatorSessionApiServiceAdapter;
import com.warehouse.auth.infrastructure.adapter.secondary.OperatorSessionRepositoryImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.OperatorSessionRevocationReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.OperatorStatusServiceAdapter;
import com.warehouse.auth.infrastructure.adapter.secondary.RefreshTokenReadRepository;
import com.warehouse.organisationstructure.api.OperatorApiService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class OperatorAccessConfiguration {

    @Bean("auth.operatorSessionRepository")
    public OperatorSessionRepository operatorSessionRepository(
            final RefreshTokenReadRepository refreshTokenReadRepository,
            final OperatorSessionRevocationReadRepository operatorSessionRevocationReadRepository) {
        return new OperatorSessionRepositoryImpl(refreshTokenReadRepository, operatorSessionRevocationReadRepository,
                Clock.systemUTC());
    }

    @Bean("auth.operatorStatusServicePort")
    public OperatorStatusServicePort operatorStatusServicePort(
            final ObjectProvider<OperatorApiService> operatorApiService) {
        return new OperatorStatusServiceAdapter(operatorApiService, Clock.systemUTC());
    }

    @Bean
    public OperatorAccessPolicy operatorAccessPolicy(final OperatorStatusServicePort operatorStatusServicePort,
                                                     final OperatorSessionRepository operatorSessionRepository) {
        return new OperatorAccessPolicyImpl(operatorStatusServicePort, operatorSessionRepository);
    }

    @Bean
    public OperatorSessionPort operatorSessionPort(final OperatorSessionRepository operatorSessionRepository) {
        return new OperatorSessionPortImpl(operatorSessionRepository, Clock.systemUTC());
    }

    @Bean
    public OperatorSessionApiService operatorSessionApiService(final OperatorSessionPort operatorSessionPort) {
        return new OperatorSessionApiServiceAdapter(operatorSessionPort);
    }
}
