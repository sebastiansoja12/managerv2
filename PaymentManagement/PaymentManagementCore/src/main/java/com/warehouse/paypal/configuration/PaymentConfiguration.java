package com.warehouse.paypal.configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.warehouse.paypal.domain.port.primary.PaypalPort;
import com.warehouse.paypal.domain.port.primary.PaypalPortImpl;
import com.warehouse.paypal.domain.port.secondary.PaymentRepository;
import com.warehouse.paypal.domain.port.secondary.PaymentSecondaryPort;
import com.warehouse.paypal.domain.service.PaymentService;
import com.warehouse.paypal.domain.service.PaymentServiceImpl;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalAdapter;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalReadRepository;
import com.warehouse.paypal.infrastructure.adapter.secondary.PaypalRepositoryImpl;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalMapper;
import com.warehouse.paypal.infrastructure.adapter.secondary.mapper.PaypalResponseMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentConfiguration {

    private final PaypalConfigurationProperties paypalConfigurationProperties;

    @Bean
    public Map<String, String> paypalSdkConfig() {
        final Map<String, String> configMap = new HashMap<String, String>();
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

    @Bean(name = "PaymentRepositoryImpl")
    public PaymentRepository paymentRepository(PaypalReadRepository readRepository) {
        final PaypalMapper paypalMapper = Mappers.getMapper(PaypalMapper.class);
        return new PaypalRepositoryImpl(readRepository, paypalMapper);
    }

    @Bean
    public PaymentSecondaryPort paymentSecondaryPort(APIContext apiContext, PaymentRepository paymentRepository) {
        final PaypalMapper paypalMapper = Mappers.getMapper(PaypalMapper.class);
        final PaypalResponseMapper responseMapper = Mappers.getMapper(PaypalResponseMapper.class);
        return new PaypalAdapter(apiContext, paypalMapper, responseMapper, paymentRepository);
    }


    @Bean
    public PaypalPort paymentPort(PaymentService paymentService) {
        return new PaypalPortImpl(paymentService);
    }

    @Bean
    public PaymentService paymentService(PaymentSecondaryPort port) {
        return new PaymentServiceImpl(port);
    }
}
