package com.warehouse.mcpkey.configuration;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.security.CredentialCipher;
import com.warehouse.commonassets.repository.NoOperatorContextBaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.mcpkey.application.port.primary.McpAccessPort;
import com.warehouse.mcpkey.application.port.primary.McpAccessPortImpl;
import com.warehouse.mcpkey.application.port.primary.McpKeyPort;
import com.warehouse.mcpkey.application.port.primary.McpKeyPortImpl;
import com.warehouse.mcpkey.application.port.secondary.McpAccessRepository;
import com.warehouse.mcpkey.application.port.secondary.McpKeyRepository;
import com.warehouse.mcpkey.domain.service.McpKeyFactory;
import com.warehouse.mcpkey.infrastructure.adapter.secondary.CurrentUserServiceAdapter;
import com.warehouse.mcpkey.infrastructure.adapter.secondary.McpAccessRepositoryImpl;
import com.warehouse.mcpkey.infrastructure.adapter.secondary.McpKeyRepositoryImpl;
import com.warehouse.mcpkey.infrastructure.adapter.secondary.UserMcpKeyServiceAdapter;
import com.warehouse.mcpkey.infrastructure.adapter.secondary.entity.McpAccessEntity;
import com.warehouse.mcpkey.infrastructure.adapter.secondary.entity.McpKeyEntity;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class McpKeyConfiguration {

    @Bean
    public McpKeyRepository mcpKeyRepository(final EntityManager entityManager,
                                             final OperatorContextProvider operatorContextProvider,
                                             final CredentialCipher credentialCipher) {
        return new McpKeyRepositoryImpl(
                new BaseRepository<McpKeyEntity>(entityManager, operatorContextProvider),
                new NoOperatorContextBaseRepository<McpKeyEntity>(entityManager), credentialCipher);
    }

    @Bean
    public McpAccessRepository mcpAccessRepository(final EntityManager entityManager,
                                                   final OperatorContextProvider operatorContextProvider) {
        return new McpAccessRepositoryImpl(new BaseRepository<McpAccessEntity>(entityManager, operatorContextProvider));
    }

    @Bean
    public McpAccessPort mcpAccessPort(final McpAccessRepository accessRepository,
                                      final McpKeyRepository keyRepository, final UserService userService,
                                      final CurrentUserApiService currentUserApiService) {
        return new McpAccessPortImpl(accessRepository, keyRepository,
                new UserMcpKeyServiceAdapter(userService, currentUserApiService), Clock.systemUTC());
    }

    @Bean
    public McpKeyPort mcpKeyPort(final McpKeyRepository mcpKeyRepository,
                                 final CurrentUserApiService currentUserApiService,
                                 final UserService userService, final McpAccessPort mcpAccessPort) {
        final Clock clock = Clock.systemUTC();
        return new McpKeyPortImpl(mcpKeyRepository, new McpKeyFactory(clock),
                new CurrentUserServiceAdapter(currentUserApiService), new UserMcpKeyServiceAdapter(userService, currentUserApiService),
                clock, mcpAccessPort);
    }
}
