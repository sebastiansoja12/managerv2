package com.warehouse.paypal.configuration;

import java.util.HashMap;
import java.util.Map;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.warehouse.paypal.domain.model.RedirectUrls;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.paypal.domain.port.primary.PaypalPortImpl;
import com.warehouse.paypal.domain.port.secondary.PaypalRepository;
import com.warehouse.paypal.domain.port.secondary.PaypalServicePort;
import com.warehouse.paypal.domain.properties.PayeeProperties;
import com.warehouse.paypal.domain.properties.PaypalConfigurationProperties;
import com.warehouse.paypal.domain.service.PaypalService;
import com.warehouse.paypal.domain.service.PaypalServiceImpl;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalAdapter;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalReadRepository;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalRepositoryImpl;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalMapper;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalRequestMapper;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalResponseMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PaymentConfiguration {

    private final PaypalConfigurationProperties paypalConfigurationProperties;

    @Bean
    public Map<String, String> paypalSdkConfig() {
        final Map<String, String> configMap = new HashMap<>();
        configMap.put("mode", paypalConfigurationProperties.getMode());
        return configMap;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        final String clientId = paypalConfigurationProperties.getClientId();
        final String clientSecret = paypalConfigurationProperties.getClientSecret();
        return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        final APIContext context = new APIContext(oAuthTokenCredential().getAccessToken());
        context.setConfigurationMap(paypalSdkConfig());
        return context;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public PaypalRepository paypalRepository(PaypalReadRepository readRepository) {
        final PaypalMapper paypalMapper = Mappers.getMapper(PaypalMapper.class);
        return new PaypalRepositoryImpl(readRepository, paypalMapper);
    }

    @Bean
    public PaypalServicePort paymentSecondaryPort(APIContext apiContext) {
        final PaypalResponseMapper responseMapper = Mappers.getMapper(PaypalResponseMapper.class);
        final PaypalRequestMapper requestMapper = Mappers.getMapper(PaypalRequestMapper.class);
        return new PaypalAdapter(requestMapper, responseMapper, paypalConfigurationProperties, apiContext);
    }


    @Bean
    public PaypalPort paymentPort(PaypalService paypalService, PayeeProperties payeeProperties) {
        return new PaypalPortImpl(paypalService, payeeProperties);
    }

	@Bean
	public PaypalService paymentService(PaypalServicePort port, PaypalRepository paypalRepository,
			RedirectUrls redirectUrls) {
		return new PaypalServiceImpl(port, paypalRepository, redirectUrls);
	}
}
